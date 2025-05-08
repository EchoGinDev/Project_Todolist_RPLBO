[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/jU8KKjiT)
# UG10_A_2024
Lengkapilah program daftar catatan sederhana ini agar memiliki fitur-fitur berikut:
## UI dan alur program
- Aplikasi terdiri dari 2 UI yang berbeda yaitu form Login dan Daftar catatan.
  ![Form Login](/img/loginform.PNG)    
  ![Form Utama](/img/mainform.PNG)  
- Untuk melakukan dapat menggunakan aplikasi catatan yang ada pada form daftar catatan, terlebih dahulu pengguna harus 
melakukan login dengan username : admin dan password : admin.
- Insert data catatan : user harus menginputkan Judul dan Isi, kemudian menekan tombol simpan. Jika Proses simpan berhasil
  maka akan menampilkan pesan "Catatan Ditambahkan!" dalam bentuk dialog box.  
- Update data catatan : Untuk merubah data catatan, user terlebih dahulu memilih catatan yang ingin dirubah dengan memilih dan
  menekan salah satu item pada table catatan. Data catatan yang dipilih akan ditampilkan pada form input, sehingga user dapat
  melakukan perubahan. Tombol simpan digunakan untuk melakukan penyimpanan data pada sistem. 
  Pesan "Catatan Dirubah!" dalam bentuk dialog box akan ditampilkan jika data berhasil dihapus.  
- Delete data catatan : Untuk menghapus data catatan, user terlebih dahulu memilih catatan yang ingin dihapus dengan memilih dan
  menekan salah satu item pada table catatan. Data catatan yang dipilih akan ditampilkan pada form input dan user dapat 
  menekan tombol hapus menghapus data pada sistem. Pesan "Catatan Dihapus!" dalam bentuk dialog box akan ditampilkan jika
  data berhasil dihapus.  
- Data aplikasi disimpan dalam SQLite.