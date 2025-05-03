<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>

<AnchorPane prefHeight="720.0" prefWidth="1280.0"
            xmlns="http://javafx.com/javafx/17.0.12"
            xmlns:fx="http://javafx.com/fxml/1"
            stylesheets="@Style.css"
            styleClass="anchor-pane">

    <VBox spacing="20.0" AnchorPane.topAnchor="20.0" AnchorPane.leftAnchor="20.0" AnchorPane.rightAnchor="20.0" AnchorPane.bottomAnchor="20.0">

        <!-- Título -->
        <Pane prefHeight="67.0" styleClass="top-bar">
            <Label alignment="CENTER" prefHeight="35.0" prefWidth="1280.0"
                   text="Gerenciamento de Reservas" styleClass="heading-main" />
        </Pane>

        <!-- Área de busca e botões -->
        <HBox spacing="10.0">
            <TextField promptText="🔍 Pesquisa" styleClass="input-field" prefHeight="35.0" HBox.hgrow="ALWAYS" />
            <Button text="Cadastrar" styleClass="custom-button" prefHeight="35.0" />
            <Button text="Editar" styleClass="custom-button" prefHeight="35.0" />
            <Button text="Excluir" styleClass="custom-button" prefHeight="35.0" />
        </HBox>

        <!-- Tabela -->
        <TableView VBox.vgrow="ALWAYS">
            <columns>
                <TableColumn text="ID" prefWidth="76.0" />
                <TableColumn text="ID Hóspede" prefWidth="100.0" />
                <TableColumn text="ID Quarto" prefWidth="90.0" />
                <TableColumn text="ID Pagamento" prefWidth="106.0" />
                <TableColumn text="Data check-in" prefWidth="195.0" />
                <TableColumn text="Data check-out" prefWidth="224.0" />
            </columns>
            <columnResizePolicy>
                <TableView fx:constant="CONSTRAINED_RESIZE_POLICY" />
            </columnResizePolicy>
        </TableView>
    </VBox>
</AnchorPane>
