<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<AnchorPane fx:id="anchorPane" prefHeight="720.0" prefWidth="1280.0" styleClass="anchor-pane" stylesheets="@Style.css" xmlns="http://javafx.com/javafx/17.0.12" xmlns:fx="http://javafx.com/fxml/1">

    <VBox prefWidth="1280.0" spacing="20.0" AnchorPane.bottomAnchor="20.0" AnchorPane.leftAnchor="20.0" AnchorPane.rightAnchor="20.0" AnchorPane.topAnchor="20.0">

        <!-- Barra superior -->
        <Pane prefHeight="67.0" styleClass="top-bar">
            <Label alignment="CENTER" layoutX="-7.0" layoutY="16.0" prefHeight="35.0" prefWidth="1280.0" styleClass="heading-main" text="Gerenciamento de Hóspedes" />
        </Pane>

        <!-- Área de busca e botões -->
        <HBox spacing="10.0">
            <TextField prefHeight="35.0" promptText="🔍 Pesquisa" styleClass="input-field" HBox.hgrow="ALWAYS" />
            <Button prefHeight="35.0" styleClass="custom-button" text="Cadastrar" />
            <Button prefHeight="35.0" styleClass="custom-button" text="Editar" />
            <Button prefHeight="35.0" styleClass="custom-button" text="Excluir" />
        </HBox>

        <!-- Tabela -->
        <TableView VBox.vgrow="ALWAYS">
            <columns>
                <TableColumn prefWidth="76.0" text="ID" />
                <TableColumn prefWidth="305.0" text="Nome" />
                <TableColumn prefWidth="142.0" text="Senha" />
                <TableColumn prefWidth="119.0" text="Nascimento" />
                <TableColumn prefWidth="303.0" text="Gmail" />
                <TableColumn prefWidth="152.0" text="CPF" />
                <TableColumn prefWidth="144.0" text="CNPJ" />
            </columns>
        </TableView>
    </VBox>
</AnchorPane>
