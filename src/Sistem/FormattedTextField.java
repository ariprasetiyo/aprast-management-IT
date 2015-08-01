/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class FormattedTextField extends JTextField {
  public FormattedTextField() {
    this(null, null, 0, null);
  }

  public FormattedTextField(String text, FormatSpec spec) {
    this(null, text, 0, spec);
  }

  public FormattedTextField(int columns, FormatSpec spec) {
    this(null, null, columns, spec);
  }

  public FormattedTextField(String text, int columns, FormatSpec spec) {
    this(null, text, columns, spec);
  }

  public FormattedTextField(Document doc, String text, int columns,
      FormatSpec spec) {
    super(doc, text, columns);
    setFont(new Font("monospaced", Font.PLAIN, 14));
    if (spec != null) {
      setFormatSpec(spec);
    }
  }

  public void updateUI() {
    setUI(new FormattedTextFieldUI());
  }

  public FormatSpec getFormatSpec() {
    return formatSpec;
  }

  public void setFormatSpec(FormattedTextField.FormatSpec formatSpec) {
    FormatSpec oldFormatSpec = this.formatSpec;

    // Do nothing if no change to the format specification
    if (formatSpec.equals(oldFormatSpec) == false) {
      this.formatSpec = formatSpec;

      // Limit the input to the number of markers.
      Document doc = getDocument();
      if (doc instanceof BoundedPlainDocument) {
        ((BoundedPlainDocument) doc).setMaxLength(formatSpec
            .getMarkerCount());
      }

      // Notify a change in the format spec
      firePropertyChange(FORMAT_PROPERTY, oldFormatSpec, formatSpec);
    }
  }

  // Use a model that bounds the input length
  protected Document createDefaultModel() {
    BoundedPlainDocument doc = new BoundedPlainDocument();

    doc
        .addInsertErrorListener(new BoundedPlainDocument.InsertErrorListener() {
          public void insertFailed(BoundedPlainDocument doc,
              int offset, String str, AttributeSet a) {
            // Beep when the field is full
            Toolkit.getDefaultToolkit().beep();
          }
        });
    return doc;
  }

  public static class FormatSpec {
    public FormatSpec(String format, String mask) {
      this.format = format;
      this.mask = mask;
      this.formatSize = format.length();
      if (formatSize != mask.length()) {
        throw new IllegalArgumentException(
            "Format and mask must be the same size");
      }

      for (int i = 0; i < formatSize; i++) {
        if (mask.charAt(i) == MARKER_CHAR) {
          markerCount++;
        }
      }
    }

    public String getFormat() {
      return format;
    }

    public String getMask() {
      return mask;
    }

    public int getFormatSize() {
      return formatSize;
    }

    public int getMarkerCount() {
      return markerCount;
    }

    public boolean equals(Object fmt) {
      return fmt != null && (fmt instanceof FormatSpec)
          && ((FormatSpec) fmt).getFormat().equals(format)
          && ((FormatSpec) fmt).getMask().equals(mask);
    }

    public String toString() {
      return "FormatSpec with format <" + format + ">, mask <" + mask
          + ">";
    }

    private String format;

    private String mask;

    private int formatSize;

    private int markerCount;

    public static final char MARKER_CHAR = '*';
  }

  protected FormatSpec formatSpec;

  public static final String FORMAT_PROPERTY = "format";
}

class BoundedPlainDocument extends PlainDocument {
  public BoundedPlainDocument() {
    // Default constructor - must use setMaxLength later
    this.maxLength = 0;
  }

  public BoundedPlainDocument(int maxLength) {
    this.maxLength = maxLength;
  }

  public BoundedPlainDocument(AbstractDocument.Content content, int maxLength) {
    super(content);
    if (content.length() > maxLength) {
      throw new IllegalArgumentException(
          "Initial content larger than maximum size");
    }
    this.maxLength = maxLength;
  }

  public void setMaxLength(int maxLength) {
    if (getLength() > maxLength) {
      throw new IllegalArgumentException(
          "Current content larger than new maximum size");
    }

    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return maxLength;
  }

  public void insertString(int offset, String str, AttributeSet a)
      throws BadLocationException {
    if (str == null) {
      return;
    }

    // Note: be careful here - the content always has a
    // trailing newline, which should not be counted!
    int capacity = maxLength + 1 - getContent().length();
    if (capacity >= str.length()) {
      // It all fits
      super.insertString(offset, str, a);
    } else {
      // It doesn't all fit. Add as much as we can.
      if (capacity > 0) {
        super.insertString(offset, str.substring(0, capacity), a);
      }

      // Finally, signal an error.
      if (errorListener != null) {
        errorListener.insertFailed(this, offset, str, a);
      }
    }
  }

  public void addInsertErrorListener(InsertErrorListener l) {
    if (errorListener == null) {
      errorListener = l;
      return;
    }
    throw new IllegalArgumentException(
        "InsertErrorListener already registered");
  }

  public void removeInsertErrorListener(InsertErrorListener l) {
    if (errorListener == l) {
      errorListener = null;
    }
  }

  public interface InsertErrorListener {
    public abstract void insertFailed(BoundedPlainDocument doc, int offset,
        String str, AttributeSet a);
  }

  protected InsertErrorListener errorListener; // Unicast listener

  protected int maxLength;
}

class FormattedTextFieldUI extends MetalTextFieldUI implements
    PropertyChangeListener {
  public static ComponentUI createUI(JComponent c) {
    return new FormattedTextFieldUI();
  }

  public FormattedTextFieldUI() {
    super();
  }

  public void installUI(JComponent c) {
    super.installUI(c);

    if (c instanceof FormattedTextField) {
      c.addPropertyChangeListener(this);
      editor = (FormattedTextField) c;
      formatSpec = editor.getFormatSpec();
    }
  }

  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    c.removePropertyChangeListener(this);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(FormattedTextField.FORMAT_PROPERTY)) {
      // Install the new format specification
      formatSpec = editor.getFormatSpec();

      // Recreate the View hierarchy
      modelChanged();
    }
  }

  // ViewFactory method - creates a view
  public View create(Element elem) {
    return new FormattedFieldView(elem, formatSpec);
  }

  protected FormattedTextField.FormatSpec formatSpec;

  protected FormattedTextField editor;
}

class FormattedFieldView extends FieldView {
  public FormattedFieldView(Element elem,
      FormattedTextField.FormatSpec formatSpec) {
    super(elem);

    this.contentBuff = new Segment();
    this.measureBuff = new Segment();
    this.workBuff = new Segment();
    this.element = elem;

    buildMapping(formatSpec); // Build the model -> view map
    createContent(); // Update content string
  }

  // View methods start here
  public float getPreferredSpan(int axis) {
    int widthFormat;
    int widthContent;

    if (formatSize == 0 || axis == View.Y_AXIS) {
      return super.getPreferredSpan(axis);
    }

    widthFormat = Utilities.getTabbedTextWidth(measureBuff,
        getFontMetrics(), 0, this, 0);
    widthContent = Utilities.getTabbedTextWidth(contentBuff,
        getFontMetrics(), 0, this, 0);

    return Math.max(widthFormat, widthContent);
  }

  public Shape modelToView(int pos, Shape a, Position.Bias b)
      throws BadLocationException {
    a = adjustAllocation(a);
    Rectangle r = new Rectangle(a.getBounds());
    FontMetrics fm = getFontMetrics();
    r.height = fm.getHeight();

    int oldCount = contentBuff.count;

    if (pos < offsets.length) {
      contentBuff.count = offsets[pos];
    } else {
      // Beyond the end: point to the location
      // after the last model position.
      contentBuff.count = offsets[offsets.length - 1] + 1;
    }

    int offset = Utilities.getTabbedTextWidth(contentBuff, metrics, 0,
        this, element.getStartOffset());
    contentBuff.count = oldCount;

    r.x += offset;
    r.width = 1;

    return r;
  }

  public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
    a = adjustAllocation(a);
    bias[0] = Position.Bias.Forward;

    int x = (int) fx;
    int y = (int) fy;
    Rectangle r = a.getBounds();
    int startOffset = element.getStartOffset();
    int endOffset = element.getEndOffset();

    if (y < r.y || x < r.x) {
      return startOffset;
    } else if (y > r.y + r.height || x > r.x + r.width) {
      return endOffset - 1;
    }

    // The given position is within the bounds of the view.
    int offset = Utilities.getTabbedTextOffset(contentBuff,
        getFontMetrics(), r.x, x, this, startOffset);
    // The offset includes characters not in the model,
    // so get rid of them to return a true model offset.
    for (int i = 0; i < offsets.length; i++) {
      if (offset <= offsets[i]) {
        offset = i;
        break;
      }
    }

    // Don't return an offset beyond the data
    // actually in the model.
    if (offset > endOffset - 1) {
      offset = endOffset - 1;
    }
    return offset;
  }

  public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
    super.insertUpdate(changes, adjustAllocation(a), f);
    createContent(); // Update content string
  }

  public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
    super.removeUpdate(changes, adjustAllocation(a), f);
    createContent(); // Update content string
  }

  // End of View methods

  // View drawing methods: overridden from PlainView
  protected void drawLine(int line, Graphics g, int x, int y) {
    // Set the colors
    JTextComponent host = (JTextComponent) getContainer();
    unselected = (host.isEnabled()) ? host.getForeground() : host
        .getDisabledTextColor();
    Caret c = host.getCaret();
    selected = c.isSelectionVisible() ? host.getSelectedTextColor()
        : unselected;

    int p0 = element.getStartOffset();
    int p1 = element.getEndOffset() - 1;
    int sel0 = ((JTextComponent) getContainer()).getSelectionStart();
    int sel1 = ((JTextComponent) getContainer()).getSelectionEnd();

    try {
      // If the element is empty or there is no selection
      // in this view, just draw the whole thing in one go.
      if (p0 == p1 || sel0 == sel1 || inView(p0, p1, sel0, sel1) == false) {
        drawUnselectedText(g, x, y, 0, contentBuff.count);
        return;
      }

      // There is a selection in this view. Draw up to three regions:
      //  (a) The unselected region before the selection.
      //  (b) The selected region.
      //  (c) The unselected region after the selection.
      // First, map the selected region offsets to be relative
      // to the start of the region and then map them to view
      // offsets so that they take into account characters not
      // present in the model.
      int mappedSel0 = mapOffset(Math.max(sel0 - p0, 0));
      int mappedSel1 = mapOffset(Math.min(sel1 - p0, p1 - p0));

      if (mappedSel0 > 0) {
        // Draw an initial unselected region
        x = drawUnselectedText(g, x, y, 0, mappedSel0);
      }
      x = drawSelectedText(g, x, y, mappedSel0, mappedSel1);

      if (mappedSel1 < contentBuff.count) {
        drawUnselectedText(g, x, y, mappedSel1, contentBuff.count);
      }
    } catch (BadLocationException e) {
      // Should not happen!
    }
  }

  protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1)
      throws BadLocationException {
    g.setColor(unselected);
    workBuff.array = contentBuff.array;
    workBuff.offset = p0;
    workBuff.count = p1 - p0;
    return Utilities.drawTabbedText(workBuff, x, y, g, this, p0);
  }

  protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1)
      throws BadLocationException {
    workBuff.array = contentBuff.array;
    workBuff.offset = p0;
    workBuff.count = p1 - p0;
    g.setColor(selected);
    return Utilities.drawTabbedText(workBuff, x, y, g, this, p0);
  }

  // End of View drawing methods

  // Build the model-to-view mapping
  protected void buildMapping(FormattedTextField.FormatSpec formatSpec) {
    formatSize = formatSpec != null ? formatSpec.getFormatSize() : 0;

    if (formatSize != 0) {
      // Save the format string as a character array
      formatChars = formatSpec.getFormat().toCharArray();

      // Allocate a buffer to store the formatted string
      formattedContent = new char[formatSize];
      contentBuff.offset = 0;
      contentBuff.count = formatSize;
      contentBuff.array = formattedContent;

      // Keep the mask for computing
      // the preferred horizontal span, but use
      // a wide character for measurement
      char[] maskChars = formatSpec.getMask().toCharArray();
      measureBuff.offset = 0;
      measureBuff.array = maskChars;
      measureBuff.count = formatSize;

      // Get the number of markers
      markerCount = formatSpec.getMarkerCount();

      // Allocate an array to hold the offsets
      offsets = new int[markerCount];

      // Create the offset array
      markerCount = 0;
      for (int i = 0; i < formatSize; i++) {
        if (maskChars[i] == FormattedTextField.FormatSpec.MARKER_CHAR) {
          offsets[markerCount++] = i;

          // Replace marker with a wide character
          // in the array used for measurement.
          maskChars[i] = WIDE_CHARACTER;
        }
      }
    }
  }

  // Use the document content and the format
  // string to build the display content
  protected void createContent() {
    try {
      Document doc = getDocument();
      int startOffset = element.getStartOffset();
      int endOffset = element.getEndOffset();
      int length = endOffset - startOffset - 1;

      // If there is no format, use the raw data.
      if (formatSize != 0) {
        // Get the document content
        doc.getText(startOffset, length, workBuff);

        // Initialize the output buffer with the
        // format string.
        System.arraycopy(formatChars, 0, formattedContent, 0,
            formatSize);

        // Insert the model content into
        // the target string.
        int count = Math.min(length, markerCount);
        int firstOffset = workBuff.offset;

        // Place the model data into the output array
        for (int i = 0; i < count; i++) {
          formattedContent[offsets[i]] = workBuff.array[i
              + firstOffset];
        }
      } else {
        doc.getText(startOffset, length, contentBuff);
      }
    } catch (BadLocationException bl) {
      contentBuff.count = 0;
    }
  }

  // Map a document offset to a view offset.
  protected int mapOffset(int pos) {
    pos -= element.getStartOffset();
    if (pos >= offsets.length) {
      return contentBuff.count;
    } else {
      return offsets[pos];
    }
  }

  // Determines whether the selection intersects
  // a given range of model offsets.
  protected boolean inView(int p0, int p1, int sel0, int sel1) {
    if (sel0 >= p0 && sel0 < p1) {
      return true;
    }

    if (sel0 < p0 && sel1 >= p0) {
      return true;
    }

    return false;
  }

  protected char[] formattedContent; // The formatted content for display

  protected char[] formatChars; // The format string as characters

  protected Segment contentBuff; // Segment pointing to formatted content

  protected Segment measureBuff; // Segment pointing to mask string

  protected Segment workBuff; // Segment used for scratch purposes

  protected Element element; // The mapped element

  protected int[] offsets; // Model-to-view offsets

  protected Color selected; // Selected text color

  protected Color unselected; // Unselected text color

  protected int formatSize; // Length of the formatting string

  protected int markerCount; // Number of markers in the format

  protected static final char WIDE_CHARACTER = 'm';
}