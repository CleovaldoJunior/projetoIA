import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Database {
    public static Connection conn;

    public static void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/projetoIA";
        conn = DriverManager.getConnection(url, "postgres","lacunar1");
        conn.setAutoCommit(false);
    }
    public static void disconnect()throws SQLException{
        conn.close();
    }
    public static void cria_tabela(String nome_tabela) throws SQLException {
        try (Statement st = conn.createStatement()){
            st.execute("CREATE TABLE IF NOT EXISTS "+nome_tabela+"()");
            conn.commit();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        connect();
        String frase = "a pearson has a name and cpf";
        List<CoreLabel> tokens = entidades(frase);
        boolean flag_entidade = false;
        ArrayList<String> atts = new ArrayList<>();
        for(CoreLabel entidade: tokens){
            String tag_atual = entidade.word();
            if(tag_atual.equals("NN")){
                flag_entidade = true;
            }
            if(flag_entidade){
                if(tag_atual.equals("NN")){
                    System.out.println(true);
                }
            }
        }
        disconnect();
    }

    public static List<CoreLabel> entidades(String text){
        Properties propriedades = new Properties();
        propriedades.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(propriedades);
        CoreDocument doc = pipeline.processToCoreDocument(text);
        return doc.tokens();
    }

}
