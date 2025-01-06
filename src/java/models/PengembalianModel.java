package models;

public class PengembalianModel {
    private String idTransaksi;
    private String merk;
    private String noPlat;
    private String nama;
    private int harga;
    private String tglPinjam;
    private int lama;
    private String tglKembali;
    private int denda;
    private int total_harga;

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public String getIdTransaksi() { 
        return idTransaksi; 
    }
    public void setIdTransaksi(String idTransaksi) { 
        this.idTransaksi = idTransaksi; 
    }

    public String getMerk() { 
        return merk; 
    }
    public void setMerk(String merk) { 
        this.merk = merk; 
    }

    public String getNoPlat() { 
        return noPlat; 
    }
    public void setNoPlat(String noPlat) { 
        this.noPlat = noPlat; 
    }

    public String getNama() { 
        return nama; 
    }
    public void setNama(String nama) { 
        this.nama = nama; 
    }

    public int getHarga() { 
        return harga; 
    }
    public void setHarga(int harga) { 
        this.harga = harga; 
    }

    public String getTglPinjam() { 
        return tglPinjam; 
    }
    public void setTglPinjam(String tglPinjam) { 
        this.tglPinjam = tglPinjam; 
    }

    public int getLama() { 
        return lama; 
    }
    public void setLama(int lama) { 
        this.lama = lama; 
    }

    public String getTglKembali() { 
        return tglKembali; 
    }
    public void setTglKembali(String tglKembali) { 
        this.tglKembali = tglKembali; 
    }

    public int getDenda() { 
        return denda; 
    }
    public void setDenda(int denda) { 
        this.denda = denda; 
    }
}
