/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
 
/*
 *  @Author Firman Hidayat
 */
 
public class dateMenghitungTanggalDanWaktu {
    // Method menghitung selisih dua waktu
    protected static String selisihDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        String selisih = selisihHari + " hari " + selisihJam + " Jam "
                + selisihMenit + " Menit " + selisihDetik + " Detik";
        return selisih;
    }
 
    protected static Date konversiStringkeDate(String tanggalDanWaktuStr,
            String pola, Locale lokal) {
        Date tanggalDate = null;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        try {
            tanggalDate = formatter.parse(tanggalDanWaktuStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return tanggalDate;
    }
 
    public static void main(String[] args) {
        Locale lokal = null;
        String pola = "dd-MMMM-yyyy HH:mm:ss:SSS";
        String waktuSatuStr = "20-Mei-2014 15:06:56:568";
        String waktuDuaStr = "18-Januari-2001 17:43:23:344";
        Date waktuSatu = konversiStringkeDate(waktuSatuStr,
                pola, lokal);
        Date WaktuDua = konversiStringkeDate(waktuDuaStr, pola,
                lokal);
        String hasilSelisih = selisihDateTime(waktuSatu,
                WaktuDua);
        System.out.println("Selisih tanggal \""+waktuSatuStr+"\" dengan tanggal \""+waktuDuaStr+"\" adalah: ");
        System.out.println(hasilSelisih);
    }
}