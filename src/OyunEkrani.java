
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class OyunEkrani extends  JFrame{ // bu class'ı JFrame'den extend(inherit) ediyoruz.
    
    

    public OyunEkrani(String title) throws HeadlessException {
        super(title); 
    }

    public static void main(String[] args) {
      OyunEkrani ekran = new OyunEkrani("Uzay Oyunu");
      ekran.setResizable(false); // oyun ekranının boyutlandırılmasını istemiyoruz
      ekran.setFocusable(false); // bütün işlemlerin jpanel üstünde olmasını istiyoruz jframe üstünde değil. bu yüzden jframe focus'u false yaptık.
      ekran.setSize(800,600); // ekran boyutunu ayarladık.
      ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Çarpıya basınca kapansın.
      Oyun oyun = new Oyun(); // jpanel'den bir nesne oluşturduk.
      oyun.requestFocus(); // klavyeden işlemleri bekler
      oyun.addKeyListener(oyun); // Klavyeden işlemleri almamızı bekler
      oyun.setFocusable(true); // focus'u jpanel üstüne topladık.
      oyun.setFocusTraversalKeysEnabled(false); // klavye işlemlerini jpanel'in anlaması için bir method
      
      ekran.add(oyun); // ekran nesnemize oyun nesnemizi ekledik.
      ekran.setVisible(true); // jframe, jpanel eklendiğinde oluşsun-gözüksün dedik.
      
      
      
      
    }
}
