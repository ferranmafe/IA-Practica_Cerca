import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class writer {
    public static void write_csv(String csv_name, ArrayList<String> params) {
        File file = new File(csv_name);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file,true));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); ++i) {
                sb.append(params.get(i));
                if (i == params.size() - 1) sb.append('\n');
                else sb.append(',');
            }
            pw.write(sb.toString());
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
