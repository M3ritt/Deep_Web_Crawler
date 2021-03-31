package DWeb_Crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class crawler{
    private String query = "";
    private final String operating_system;

    public crawler(){
        this.operating_system = System.getProperty("os.name");
    }


    public void startup(){
        System.out.println("\n[Disclaimer]\nThe software is provided as is, without warranty of any kind, express or implied, " +
                "including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringment.\n" +
                "In no event shall the authors or copyright holder be liable for any claim, damages or other liability, whether in an " +
                "action of contract, tort or otherwise, arising from, out of or in connection with the software\n" +
                "or the use of other dealings in the software. \n" +
                "@User [Continue]:");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("continue")){
            clear_console();
            System.out.println("" +
                    "                 <>              \n" +
                    "               .::::.             \n" +
                    "           @\\\\/W\\/\\/W\\//@         \n" +
                    "            \\\\/^\\/\\/^\\//     \n" +
                    "             \\_O_<>_O_/ \n" +
                    "    XXXXXXXXXXXXXXXXXXXXXXXXXXXXX \n" +
                    "  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX \n" +
                    "  XXXXXXXXXXXXXXXXXX         XXXXXXXX \n" +
                    "XXXXXXXXXXXXXXXX              XXXXXXX    ______                  _____                     _     \n" +
                    "XXXXXXXXXXXXX                   XXXXX    |  _  \\                /  ___|                   | |    \n" +
                    " XXX     _________ _________     XXX     | | | |___  ___ _ __   \\ `--.  ___  __ _ _ __ ___| |__  \n" +
                    "  XX    |xxxxxxxxxIxxxxxxxxx|    XX      | | | / _ \\/ _ \\ '_ \\   `--. \\/ _ \\/ _` | '__/ __| '_ \\ \n" +
                    " ( X----|xxxxxxxxxIxxxxxxxxx|----| )     | |/ /  __/  __/ |_) | /\\__/ /  __/ (_| | | | (__| | | |\n" +
                    "( +|----|xxxxxxxxxIxxxxxxxxx|----|+ )    |___/ \\___|\\___| .__/  \\____/ \\___|\\__,_|_|  \\___|_| |_|\n" +
                    " ( |    |xxxxxxxxxIxxxxxxxxx|    | )                    | |\n" +
                    "  (|    |______ /   \\_______|    |)                     |_|\n" +
                    "   |           ( ___ )           |\n" +
                    "   |    _  :::::::::::::::  _    |                       Developed by: Josh Meritt\n" +
                    "    \\    \\___ ::::::::: ___/    /   \n" +
                    "     \\_      \\_________/     _/ \n" +
                    "       \\        \\___,      / \n" +
                    "        \\                / \n" +
                    "        |\\              /| \n" +
                    "        |  \\__________/  |\n\n");
            get_search_phrase();
        }
        System.out.println("[Exiting]");
        clear_console();
    }


    public void clear_console(){
        try {
            if(this.operating_system.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch(IOException | InterruptedException e){
            System.out.println("[Error]");
        }
    }


    public void get_search_phrase(){
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("@User [Enter search term(s)]: ");
            String searched = sc.nextLine();
            if(searched.equalsIgnoreCase("exit"))
                return;
            create_query(searched);
            search();
        }
    }


    public void create_query(String searched){
        //       -page:some_number can change the page is being searched
        int index = searched.indexOf(" -page:");
        if(index != -1){
            int page_num = Integer.parseInt(searched.substring(index+7));
            searched = searched.substring(0, index);
            create_query_helper(searched);
            this.query += "&page="+page_num;
        } else {
                create_query_helper(searched);
        }
    }


    public void create_query_helper(String searched){
        String[] arr = searched.split(" ");
        ArrayList<String> data = new ArrayList<>(Arrays.asList(arr));
        int pos = 1;
        String api = "https://darksearch.io/api/search?query=";
        this.query = api;
        for (String s : data) {
            String additional_query = "&query=";
            if (pos == 1) {
                this.query += s;
                pos++;
            } else this.query += additional_query + s;
        }
    }


    public void search(){
        StringBuilder result = new StringBuilder();
        String string_result = get_request(result);
        if(string_result != "[Error]") {
            ArrayList<Website> data = remove_start_info(string_result);
            System.out.println("\n===================================================\n\n[Results]: " + data.size());
            for (Website w : data)
                System.out.println(w + "\n");
        } else System.out.println("[Error] Invalid request");
    }


    public String get_request(StringBuilder result){
        try {
            URL url = new URL(this.query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine() ) != null){
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch(IOException e){
            return "[Error]";
        }
    }


    public ArrayList<Website> remove_start_info(String result){
        int startIndex = result.indexOf("[{");
        int endIndex = result.indexOf("}]");
        result = result.substring(startIndex+1, endIndex+1);
        return create_ArrayList(result);
    }


    public ArrayList<Website> create_ArrayList(String result){
        String[] arr = result.split("},");
        ArrayList<String> data = new ArrayList<>(Arrays.asList(arr));
        return remove_doubles(data);
    }


    public ArrayList<Website> remove_doubles(ArrayList<String> data){
        ArrayList<Website> new_data = new ArrayList<>();
        for(String s : data){
            Website current_website = create_website(s);
            if(!is_in_list(s, new_data) && current_website != null)
                new_data.add(current_website);
        }
        return new_data;
    }


    public boolean is_in_list(String data, ArrayList<Website> sites){
        for(Website s : sites){
            if(data.contains(s.getTitle()))
                return true;
        }
        return false;
    }


    public Website create_website(String data){
        String title;
        String link;
        String description;
        try {
            title = data.substring(data.indexOf("title") + 7, data.indexOf("link"));
            link = data.substring(data.indexOf("link") + 6, data.indexOf("description"));
            description = data.substring(data.indexOf("description") + 13);
        }catch (IndexOutOfBoundsException e){
            return null;
        }

        title = title.replaceAll("\"S", "S").replaceAll(",", "").replace("\"\"", "\"").replaceAll("\\\\", "");
        if(!link.contains("onion") || title.contains("\\n"))
            return null;
        link = link.replaceAll("\\\\", "").replaceAll(",\"", "");
        return new Website(title, link, description);
    }
}
