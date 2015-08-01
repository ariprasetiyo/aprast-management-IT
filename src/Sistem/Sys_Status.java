/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arprast
 * 
     ari prasetiyo
     modify - 1 - 21 desember 2014 by ari prasetiyo
     Memilah milah OS
     
 */
public class Sys_Status {
    public Sys_Status(){
        
    }
    /*
     * Medapatkan versi OS
     */
     
     public String OSName (){
         String OS;
         OS= System.getProperty("os.name").toLowerCase()
                 +" ("+System.getProperty("os.arch").toLowerCase()
                 +") build "+System.getProperty("os.version").toLowerCase();
         return OS;
     }
     /*
      * Mendapatkan HostName
      */
     
     public String HostName (){
         String  Host = null;
         InetAddress IpLocal;
         try {
            IpLocal = InetAddress.getLocalHost();
            Host = IpLocal.getHostName();
            return Host;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Sys_Status.class.getName()).log(Level.SEVERE, null, ex);
        }
         return Host;
     }
     /*
      * Mendapatkan iplocal
      */
     String  IPLocal;
     public String IPLocal (){
        try {
            return getLocalAddress().getHostAddress();
        } catch (SocketException ex) {
            Logger.getLogger(Sys_Status.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
     }
     
     public static InetAddress getLocalAddress() throws SocketException
        {
          Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
          while( ifaces.hasMoreElements() )
          {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> addresses = iface.getInetAddresses();

            while( addresses.hasMoreElements() )
            {
              InetAddress addr = addresses.nextElement();
              if( addr instanceof Inet4Address && !addr.isLoopbackAddress() )
              {
                return addr;
              }
            }
          }
    return null;
  }
     /*
     Ambil ip v6 dan v4
     */
    public void iplocal3(){

        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                     /*
                    System.out.println(iface.getDisplayName() + "  xxxx" + ip);
                    display = interface
                    ip      = display ip

                    */
                    System.out.println(iface.getDisplayName() + "  xxxx " + ip);
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    } 
     /*
     pilihan iplocal
     1. ubuntu window, interfaca apapun jalan
     */
     private String iplocal1() throws IOException{
         
        String command = null;
        if(System.getProperty("os.name").equals("Linux"))
            command = "ifconfig";
        else
            command = "ipconfig";
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        Scanner s = new Scanner(p.getInputStream());

        StringBuilder sb = new StringBuilder("");
        while(s.hasNext())
            sb.append(s.next());
        String ipconfig = sb.toString();
        Pattern pt = Pattern.compile("192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher mt = pt.matcher(ipconfig);
        mt.find();
        String ipx = mt.group();
        return ipx;            
     }
     
     /*
     2. iplocal 2
     yang ditampilkan interface lan dan bisa return 127.0.0.1
     */
     private String iplocal2() {
                 
        InetAddress IpLocal;
        try {
        IpLocal = InetAddress.getLocalHost();
        IPLocal = IpLocal.getHostAddress();
        return IPLocal;
        } catch (UnknownHostException ex) {
        Logger.getLogger(Sys_Status.class.getName()).log(Level.SEVERE, null, ex);
        }
        return IPLocal; 
     }
     
     /*
      * Mendapatkan ippublic
      */
    
     public String IPPublic () {
        String IPPublik = null;
        InetAddress IpLocal = null;
        String IpLocalData;
        try {
            // Mendeteksi Ip Public
            URL url= new URL("http://gt-tests.appspot.com/ip");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            IPPublik = in.readLine();
            //System.out.print(ip);
            return IPPublik;
        } catch (IOException ex) {
            Logger.getLogger(Sys_Status.class.getName()).log(Level.SEVERE, null, ex);
        }
        return IPPublik;
    }
     
     /*
     Database
     */
     public String IPServer, Database;
     public void StatusDatabase(){
        ArrayList Data = new ArrayList();
        DB_SQLite x = new DB_SQLite();
        String [] Col = { "ip","dbname"};
        Data = x.SelectQuery("select * from server ", Col);
        try {
            Database =  Data.get(1).toString();
            IPServer =  Data.get(0).toString();
        }
        catch (Exception xx) {
            Database = null;
            IPServer = null;
        }
     }
     
    /*
     ari prasetiyo
     creted     21 Desember 2014
     Memilah milah OS
     */
    private static String oS (){
        String os = System.getProperty("os.name").toLowerCase();
        return os;
    }
    public  boolean isWindows() {
            return (oS().contains("win"));
    }

    public  boolean isMac() {
            return (oS().contains("mac"));
    }

    public  boolean isUnix() {
            return (oS().contains("nix") || oS ().contains("nux") || oS ().indexOf("aix") > 0 );
    }

    public  boolean isSolaris() {
            return (oS().contains("sunos"));
    }
     
}
