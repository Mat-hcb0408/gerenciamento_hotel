<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<AnchorPane prefHeight="720.0" prefWidth="1280.0" styleClass="anchor-pane" stylesheets="@Style.css" xmlns="http://javafx.com/javafx/17.0.12" xmlns:fx="http://javafx.com/fxml/1">

   <VBox spacing="20.0" AnchorPane.bottomAnchor="20.0" AnchorPane.leftAnchor="20.0" AnchorPane.rightAnchor="20.0" AnchorPane.topAnchor="20.0">

      <!-- Barra superior -->
      <Pane prefHeight="67.0" styleClass="top-bar">
         <Label alignment="CENTER" layoutX="102.0" layoutY="16.0" prefHeight="35.0" prefWidth="1280.0" styleClass="heading-main" text="Gerenciamento de Reservas" />
      </Pane>

      <!-- Área de busca e botões -->
      <HBox spacing="10.0">
         <TextField prefHeight="35.0" promptText="🔍 Pesquisa" styleClass="input-field" HBox.hgrow="ALWAYS" />
         <Button prefHeight="35.0" styleClass="custom-button" text="Cadastrar" />
         <Button prefHeight="35.0" styleClass="custom-button" text="Editar" />
         <Button prefHeight="35.0" styleClass="custom-button" text="Excluir" />
      </HBox>

      <!-- Tabela de Reservas -->
      <TableView fx:id="tabelaReservas" VBox.vgrow="ALWAYS">
         <columns>
            <TableColumn fx:id="colunaId" prefWidth="76.0" text="ID" />
            <TableColumn fx:id="colunaHospede" prefWidth="100.0" text="ID Hóspede" />
            <TableColumn fx:id="colunaQuarto" prefWidth="90.0" text="ID Quarto" />
            <TableColumn fx:id="colunaPagamento" prefWidth="106.0" text="ID Pagamento" />
            <TableColumn fx:id="colunaCheckin" prefWidth="195.0" text="Data check-in" />
            <TableColumn fx:id="colunaCheckout" prefWidth="224.0" text="Data check-out" />
         </columns>
         <columnResizePolicy>
            <TableView fx:constant="CONSTRAINED_RESIZE_POLICY" />
         </columnResizePolicy>
      </TableView>
   </VBox>
</AnchorPane>
