module gr.uop.asteras {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.lucene.core;
    requires org.apache.lucene.analysis.common;
    requires org.apache.lucene.queryparser;


    opens gr.uop.asteras to javafx.fxml;
    exports gr.uop.asteras;
}