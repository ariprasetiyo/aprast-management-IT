/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

/**
 *
 * @author arprast
 */
public class mainLink {
    


            public static void main(String[] args)
            {
                    LinkList objek = new LinkList();
                   
                    objek.tambahDepan(10);
                    objek.tampilkanList();
                    objek.tambahDepan(3);
                    objek.display();
                    objek.tampilkanList();
                    objek.tambahDepan(12);
                    objek.tambahDepan(19);
                    objek.tambahBelakang(3);
                    objek.tambahBelakang(1);
                    objek.tambahBelakang(9);
                    objek.hapusBelakang();
                   
            }
            
  
    
    

    }

class Node
    {
            public int data;
            public Node next;
            //---------------------------------------
            public Node(int d){
                    data = d;
                    next = null;
            }
     
            public void tampilkanLink(){
                    System.out.print("{" + data + "," + null + "}");
            }
    }
    

class LinkList
    {
            Node head, tail;
     
            //======================================
            public LinkList(){
                    head = null;
            }
     
            public boolean isEmpty(){
                    return(head == null);
            }
     
            public void tambahDepan(int d){
                    Node baru = new Node(d);
                    if (head==null)
                    {
                            head = baru;
                            tail = baru;
                    }
                    else{
                            baru.next = head;
                            head = baru;
                    }
            }
            public void tambahBelakang(int d){
                    Node baru = new Node(d);
                    if (head == null)
                    {
                            head = baru;
                            tail = baru;
                    }
                    else{
                            tail.next = baru;
                            tail = baru;
                    }
            }
            public Node hapusDepan(){
                    if (head!=null)
                    {
                            Node temp = head;
                            head = head.next;
                            temp.next = null;
                            return temp;
                    }
                    else{
                            System.out.println("list kosong");
                            return null;
                    }
            }
            public Node hapusBelakang(){
                    if (head!=null)
                    {
                            Node bantu, temp;
                            if (head.next == null)
                            {
                                    temp = head;
                                    head = tail = null;
                            }
                            else{
                                    bantu = head;
                                    while (bantu.next!=tail)
                                    {
                                            bantu = bantu.next;
                                    }
                                    temp = tail;
                                    tail = bantu;
                                    tail.next = null;
                            }
                            return temp;
                    }
                    else{
                            System.out.println("List Kosong");
                            return null;
                    }
            }
     
     
            public void display(){
                    Node theLink = head;
                    Node thseLink = tail;
     
                    while (theLink != null)
                    {
                            System.out.println("Next Link : " + theLink.next);
                            theLink = theLink.next;
                            System.out.println();
     
                    }
            }
            public void tampilkanList(){
                    System.out.print("List(pertama -->> terakhir):");
                    Node sekarang = head;
                    while (sekarang != null)
                    {
                            sekarang.tampilkanLink();
                            sekarang = sekarang.next;
                    }
                    System.out.println("");
            }
     
     
    }