import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.sql.*;
import java.util.ArrayList;
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

    public static ArrayList<String> NamedEntity(String text){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);
        ArrayList<String> tabelas = new ArrayList<>();
        for (CoreEntityMention em : doc.entityMentions()) {

            tabelas.add(em.entityType());
        }
        return tabelas;
    }

    public static ArrayList<ArrayList<String>> pega_entidades(String frase) throws SQLException {
        connect();
        ArrayList<ArrayList<String>> entidades_geral = new ArrayList<>();
        List<CoreLabel> tokens = entidades(frase);
        boolean flag_entidade = false;
        ArrayList<String> atts_atual = new ArrayList<>();
        for(CoreLabel entidade: tokens){
            ArrayList<String> entidade_atual = NamedEntity(entidade.word());
            System.out.println(entidade.word()+" "+entidade.tag()+" "+entidade_atual);
            String tag_atual = entidade.tag();
            if(flag_entidade){
                if(entidade_atual.isEmpty()){
                    if(tag_atual.equals("NN")){
                        atts_atual.add(entidade.word());
                    }
                }else{
                    atts_atual.add(entidade_atual.get(0));
                }
            }
            if(((tag_atual.equals("NN") || (tag_atual.equals("NNP"))) && !flag_entidade)){
                if(entidade_atual.isEmpty()){
                    atts_atual.add(entidade.word());
                }else{
                    atts_atual.add(entidade_atual.get(0));
                }
                flag_entidade = true;
            }else if(tag_atual.equals("NNP")){
                ArrayList<String> copia = new ArrayList<>(atts_atual);
                entidades_geral.add(copia);
                atts_atual.clear();
                if(entidade_atual.isEmpty()){
                    atts_atual.add(entidade.word());
                }else{
                    atts_atual.add(entidade_atual.get(0));
                }

            }
            System.out.println("atts_atual: "+atts_atual);
        }
        entidades_geral.add(atts_atual);
        return entidades_geral;
    }

    public static void run(String frase) throws SQLException {
        ArrayList<ArrayList<String>> entidades = pega_entidades(frase);
        for(ArrayList<String> celula: entidades){
            System.out.println(celula.get(0));
            cria_tabela(celula.get(0));
        }
    }

    public static List<CoreLabel> entidades(String text){
        Properties propriedades = new Properties();
        propriedades.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(propriedades);
        CoreDocument doc = pipeline.processToCoreDocument(text);
        return doc.tokens();
    }

}
