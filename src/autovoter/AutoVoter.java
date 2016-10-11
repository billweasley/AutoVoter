package autovoter;

import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutoVoter extends Thread {

    final private String sid = "3";
    final private String star_id = "391";
    final private String client = "pcweb";
    private String uuid;
    private String ticket;
    private Map<String, String> cookies;
    private String account;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AutoVoter(Map<String, String> cookies) {

        this.cookies = cookies;
        this.uuid = cookies.get("uuid");
        this.ticket = cookies.get("HDCN").substring(0, 20);
        this.account = cookies.get("account");
    }

    public static void main(String[] args) {
        CookieGetter cg = new CookieGetter();
        List<AutoVoter> toExecuteItems = new ArrayList<>();

        AutoVoter av;
        List<Map<String, String>> recoveryRecords = null;
        try {
            recoveryRecords = new FileOpearter().readRecord();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<String> toRemoveRep = new ArrayList<>();

        for (Map<String, String> recRecord : recoveryRecords) {
            av = new AutoVoter(recRecord);
            toExecuteItems.add(av);
            toRemoveRep.add(recRecord.get("account"));
        }
        List<Map<String, String>> accounts = null;
        try {
            if (recoveryRecords.isEmpty()) {
                accounts = new FileOpearter().readAccount();
            } else {
                accounts = new FileOpearter().readAccount(toRemoveRep);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Map record = null;
        for (Map<String, String> account : accounts) {

            cg.setAccount(account.get("account"));
            cg.setPwd(account.get("password"));
            record = cg.login();
            av = new AutoVoter(record);
            toExecuteItems.add(av);
        }
        cg.getDriver().close();
        for (AutoVoter a : toExecuteItems) {
            a.start();
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
            out.write("");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String url = "http://eagle.api.mgtv.com/vote.php?" + "sid=" + sid + "&star_id=" + star_id + "&client=" + client + "&uuid=" + uuid + "&ticket=" + ticket;
        Connection con = Jsoup.connect(url).timeout(30000000);
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
        con.header("Connection", "Keep-Alive");
        con.header("Host", "eagle.api.mgtv.com");
        con.header("DBT", "1");
        con.referrer("http://www.mgtv.com/v/2016/jyj2016/");
        con.method(Method.GET);
        con.cookies(cookies);

        for (int i = 0; i < 23; ) {
            Response vote = null;
            try {
                vote = con.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (vote.body().startsWith("{\"code\":200")) {
                System.out.println(account + " 投票成功" + df.format(new Date()));

            } else if (vote.body().startsWith("{\"code\":-1007")) {
                System.out.println(account + " 还不到一个小时！" + df.format(new Date()));
            } else {
                System.out.println(account + " 投票失败。" + df.format(new Date()));
            }
                System.out.println( "来自服务器的原始回复消息：" + vote.statusMessage());
                System.out.println( "来自服务器的原始回复消息体：" + vote.body());
                System.out.println( "——————————————————————");

            try {
                Thread.sleep(1000 * 60 * 60 + 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
