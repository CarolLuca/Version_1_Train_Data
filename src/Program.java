import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.sort;

public class Program {
    public static void main(String[] args) throws IOException {
        PrintWriter fout = new PrintWriter("src/files_overlap.txt");
        System.out.print("TRAIN_DATA = [\n");
        File filenames = new File("src/filenames.txt");
        File filenamesT = new File("src/filenamesT.txt");
        Scanner in_fileA = new Scanner(filenames);
        Scanner in_fileT = new Scanner(filenamesT);
        in_fileA.nextLine();
        in_fileT.nextLine();
        for (int no = 0; no < 370; no++) {
            String ann_fileS = in_fileA.nextLine();
            String txt_fileS = in_fileT.nextLine();
            File ann_file = new File("src/ann_LEGAL_PER_LOC_ORG_TIME/" + ann_fileS);
            File txt_file = new File("src/text/" + txt_fileS);
            Scanner inA = new Scanner(ann_file);
            Scanner inT = new Scanner(txt_file);
            List<Trois> entp = new ArrayList<>();
            List<Trois> ent = new ArrayList<>();
            while (inA.hasNextLine()) {
                String lineA = inA.nextLine();
                char[] ch = lineA.toCharArray();
                int cnt = 0, ind = 0;
                String type = "";
                int start = 0;
                while (cnt < 4) {
                    int lastCnt = cnt;
                    if (!Character.isLetterOrDigit(ch[ind])) {
                        ++cnt;
                    }
                    if (lastCnt == 1 && cnt == 1) {
                        type = type + ch[ind];
                    }
                    if (lastCnt == 2 && cnt == 2) {
                        start = start * 10 + (ch[ind] - '0');
                    }
                    ++ind;
                }
                String entity = "";
                for (int i = ind; i < ch.length; ++i) {
                    entity = entity + ch[i];
                }
                entity = entity.replace("\"", " ");
                entp.add(new Trois(start, entity, type));
            }
            sort(entp);
            ent.add(entp.get(0));
            for (int i = 1; i < entp.size(); ++i) {
                if (entp.get(i).getStart() >= entp.get(i - 1).getStart() + entp.get(i - 1).getEntity().length()) {
                    ent.add(entp.get(i));
                }
                /*else {
                    System.out.println(entp.get(i-1).getEntity());
                    System.out.println(entp.get(i).getEntity());
                    return ;
                }*/
            }
            if(ent.size()!=entp.size()){
                fout.println(txt_fileS);
                // return ;
            }
            String line = inT.nextLine();
            line = line.replace("\"", " ");
            // String visited = line;
            boolean same = false;
            System.out.print("(\"" + line + "\",[");
            for (int i = 0; i < ent.size(); ++i) {
                String caut = ent.get(i).getEntity();
                int n = caut.length();
                boolean foundE = false;
                while (!foundE) {
                    for (int j = 0; j + n <= line.length(); ++j) {
                        if (line.substring(j, j + n).equals(caut)){ // && visited.charAt(j)!='w') {
                            foundE = true;
                            if (same) {
                                System.out.print(",");
                            }
                            /*
                            StringBuilder builderV = new StringBuilder(visited);
                            builderV.replace(j,j+1,"w");
                            visited = builderV.toString();
                            */
                            same = true;
                            System.out.print("(" + j + "," + (j + n) + ",'" + ent.get(i).getType() + "')");
                        }
                    }
                    if (!foundE) {
                        System.out.print("]),\n");
                        line = inT.nextLine();
                        line = line.replace("\"", " ");
                        // visited = line;
                        System.out.print("(\"" + line + "\",[");
                        same = false;
                    }
                }
            }
            System.out.print("]),\n");
            while (inT.hasNextLine()) {
                line = inT.nextLine();
                line = line.replace("\"", " ");
                // visited = line;
                System.out.print("(\"" + line + "\",[]),\n");
            }
        }
        System.out.print("]\n");
        fout.close();
    }
}
