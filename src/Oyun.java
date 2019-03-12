
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Rectangle;
import javax.swing.JOptionPane;

class Ates {

    private int x; // x ve y koordinatlarında hareket edecek.
    private int y;

    public Ates(int x, int y) { // constructor oluşturduk
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

public class Oyun extends JPanel implements KeyListener, ActionListener { // KeyListener Interface -> klavyeden tuş bekler,ActionListener Interface -> actionperform içindeki methodu kullanacağız. timer her çalıştığında actionperform çalışacak ve top hareket edecek

    Timer timer = new Timer(1, this); // 2 parametre bekler. 1 -> kaç milisaniyede bir çalışacak, 2 -> actionlistener implemente eden obje göndermeliyiz. bizim jpanel'imiz actionlistener implemente ettiği için this yazdık.
    private int gecenSüre = 0; // geçen süreyi hesaplamak istiyoruz bunun için değişken oluşturduk
    private int harcananAtes = 0; // kaç atış yaptığımızı saymak istiyoruz
    private BufferedImage image; // image okumak-yüklemek için 
    private ArrayList<Ates> atesler = new ArrayList<Ates>(); // atılan ateşleri bir array list içinde tutmak için arraylist oluşturduk (Ates türünde değerler için)

    private int atesdirY = 1; // ateşler bir yerde oluşacak ve her actionperformed olduğunda ateşlerimizi y koordinatına ekleyeceğiz.
    private int topX = 0; // top'un sağa sola gitmesini ayarlayacak
    private int topdirX = 2; // topX 'e eklenecek ve sağda solda kenarlara çarptıkça top öbür tarafa dönecek
    private int uzayGemisiX = 0; // tam alt sol'dan başlayacak.
    private int dirUzayX = 20; // klavyede sağ'a bastığımızda bu dirUzayX'i uzayGemisiX'e ekleyeceğiz ve böylelikle gemi hareket edecek.

    public boolean kontrolEt() { // eğer ateş ile top çarpışırsa true yoksa false döndüreceğiz bundan dolayı boolean method oluşturduk
        for (Ates ates : atesler) {
            if (new Rectangle(ates.getX(),ates.getY(),10,20).intersects(new Rectangle(topX,0,20,20))) {// ilk Rectangle(koordinatları ve boyutlarını verdik) ile ikinci rectangle(koordinatları ve boyutlarını verdik(hep topx düzleminde ve 0 y sinde 20,20 boyutunda)) çarpışırsa
                return true; // true döndür
                
            }
        }
        return false; // çarpışma olmazsa false döndür
    }

    public Oyun() { // constructor oluşturduk. oyun başladığında bu blok çalışacak.
        String message = "Sağa gitmek için -> tuşuna\n"+"Sola gitmek için <- tuşuna\n" + "Ateş etmek için 'ctrl' basın";
        JOptionPane.showMessageDialog(this, message);
        try {
            image = ImageIO.read(new FileImageInputStream(new File("pic.png"))); // uzay gemisini oyuna dahil ettik.
        } catch (IOException ex) {
            System.out.println("HATA VAR!*******************"); // bu oyunu yazarken anladığım bir şey : bu try-catch blokları daha efektif kullanmalıyız. sadece syntax hatası olmasın diye değil, gerçekten hataları görüp gerekli aksiyonları alabilmek için develop esnasında kullanılmalı.
            System.out.println(ex.getMessage());
        }
        setBackground(Color.black); // arka planı siyah yaptık.
        timer.start(); // timer başladığında bütün actionPerformed bloğu çalışacak (107. satır)

    }

    @Override
    public void paint(Graphics g) { // paint methodunu override ettik.
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        gecenSüre += 5; // gecensüreye 5 milisaniye ekledik
        g.setColor(Color.red); // topun rengini belirledik
        g.fillOval(topX, 0, 20, 20); // top her hareket ettiğinde topX güncellenecek. 20,20 -> top büyüklüğü
        g.drawImage(image, uzayGemisiX, 490, image.getWidth() / 10, image.getHeight() / 10, this);
        //image -> g'nin şekli, uzayGemisiX -> x koordinatı, 490 -> y koordinatı (nereden başlayacağı), gedwidth/10 ve getheight / 10 -> geminin boyu, this -> bu jpanel üzerine geleceği
        for (Ates ates : atesler) { // for each döngüsü oluşturyoruz. oluşan her ateşin yeniden çizilmesi için bunu paint methodu içinde yazıyoruz.
            if (ates.getY() < 0) { // ates jframe'den çıkarsa
                atesler.remove(ates); // bu ateşi sil dedik
            }
        }
        g.setColor(Color.blue); // ateslerin rengini belirliyoruz
        for (Ates ates : atesler) {
            g.fillRect(ates.getX(), ates.getY(), 10, 20); // 10,20 -> kaça kaçlık bir dörtgen. 
            // x ve y'sini belirledik
        }
        if (kontrolEt()) { // eğer kontrolEt method true dönerse
            timer.stop(); // timer durdur
            String message = "Kazandınız...\n" + "Harcanan ateş : " + harcananAtes +"\nGeçen Süre : " + gecenSüre / 1000.0 + " saniye"; // mesaj değeri oluşturduk.
            JOptionPane.showMessageDialog(this,message); // this -> bu jframe'de
            System.exit(0); // programı kapat
            
        }

    }

    @Override
    public void repaint() { // repaint methodunu override ettik.
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode(); // gelen parametrenin keycode methodu ile
        if (c == KeyEvent.VK_LEFT) { // eğer key event sol tuşa basılmışsa dedik
            if (uzayGemisiX <= 0) { // eğer uzayGemisiX koordinatı 0 veya küçükse
                uzayGemisiX = 0; // koordinatı 0'a eşitle
            } else {
                uzayGemisiX -= dirUzayX; // eğer yukardaki koşul sağlanmazsa olduğu konumun koordinatından 20 çıkacak ve sola hareket etmiş olacak
            }
        } else if (c == KeyEvent.VK_RIGHT) { // eğer sağa basılırsa
            if (uzayGemisiX >= 720) { // eğer koordinatı 720 veya büyükse
                uzayGemisiX = 720;  // koordinatı 720'ye eşitle
            } else {
                uzayGemisiX += dirUzayX; // değilse o anki koordinata 20(dirUzayX kadar) ekle ve böylece sağa kaymış olsun
            }

        } else if (c == KeyEvent.VK_CONTROL) { // ctrl tuşuna basılmışsa
            atesler.add(new Ates(uzayGemisiX + 15, 470)); // Ates class'ın field ve attributeleri parametre olarak verdik. 490 -> y'si uzaymekiği ile aynı yerde(hep 490 koordinatından başladığı için). uzay gemisi herhangi bir yerde ise o koordinatı vermek içinde uzayGemisiX parametresi verilir. +15 olmasının sebebi ise bizim gemi bir image'den oluşuyor. bu yüzden +15 verip tam ucuna getirmeye çalışıyoruz.
            harcananAtes++; // ctrl her bastığımızda yukarda tanımladığımız değişken artsın diye. bunun sebebi ise oyun sonunda kaç atış yapılmış onu yazdıracağız.

        }

    }

    @Override
    public void keyReleased(KeyEvent arg0) {

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        for (Ates ates : atesler) {
            ates.setY(ates.getY() - atesdirY); // ateşlerin koordinatları değişecek

        }

        topX += topdirX;
        if (topX >= 750) { // 750. koordinattan büyük olduğunda geri sarmaya başlayacağız
            topdirX = -topdirX; // topdirX -2 olacağı için geri gitmeye başlayacak.

        }
        if (topX <= 0) { // 0. koordinata geldiğinde
            topdirX = -topdirX; // öbür tarafa gitmesini sağlayacağız.
        }
        repaint(); // bu method sayesinde paint methodu sürekli çalışacak ve top sürekli güncelleneceğinden hareket etmiş gibi gözükecek.

    }

}
