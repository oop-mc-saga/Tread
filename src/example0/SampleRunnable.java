package example0;

/**
 * RunnableにしたSmapleクラス
 * @author tadaki
 */
public class SampleRunnable extends Sample implements Runnable {

    public SampleRunnable(int id) {
        super(id);
    }

    /**
     * ランダムな時間間隔でupdate()を実行
     */
    @Override
    public void run() {
        while (running) {
            update();
            int t = (int) (1000 * Math.random());
            try {
                Thread.sleep(t);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Thread(new SampleRunnable(1)).start();
        new Thread(new SampleRunnable(2)).start();
        Thread t = new Thread(new SampleRunnable(3));
        t.start();
    }

}
