/* Fundo geral */
.anchor-pane {
    -fx-background-color: #1e1e30;
}

/* Barra superior */
.top-bar {
    -fx-background-color: #2a2a40;
    -fx-border-radius: 15;
}

/* Título principal */
.heading-main {
    -fx-text-fill: white;
    -fx-font-size: 24px;
    -fx-font-family: "Bell MT Bold";
}

/* Caixa ao centro */
.form-card {
    -fx-background-color: #2a2a40;
    -fx-background-radius: 16;
    -fx-padding: 30;
    -fx-max-width: 600;
}

/* Label dos campos */
.form-label {
    -fx-text-fill: white;
    -fx-font-size: 14px;
}

/* Campo de entrada (TextField, PasswordField, etc.) */
.input-field {
    -fx-background-color: transparent;
    -fx-text-fill: white;
    -fx-font-size: 16px;
    -fx-border-width: 0;
    -fx-focus-color: transparent;
    -fx-faint-focus-color: transparent;
}

/* Placeholder */
.input-field .text {
    -fx-prompt-text-fill: rgba(255, 255, 255, 0.5);
}

/* Quando focado */
.input-field:focused {
    -fx-border-color: white;
    -fx-border-width: 0 0 2 0;
}

/* Link de texto */
.link-text {
    -fx-text-fill: white;
    -fx-font-size: 11px;
}

/* Hyperlink branco */
.white-hyperlink {
    -fx-text-fill: white;
    -fx-border-color: transparent;
    -fx-underline: true;
}

.white-hyperlink:hover {
    -fx-text-fill: #cccccc;
}

/* Mensagem de boas-vindas */
.welcome-message {
    -fx-font-size: 20px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
    -fx-alignment: center;
    -fx-text-alignment: center;
}

/* ===== Botões Estilizados - Versão Arredondada ===== */
.custom-button {
    -fx-font-family: "Bell MT Bold";
    -fx-font-size: 16px;
    -fx-text-fill: #7b2cbf;
    -fx-background-color: transparent;
    -fx-border-color: #7b2cbf;
    -fx-border-radius: 20;  /* Bordas muito arredondadas */
    -fx-border-width: 2;
    -fx-cursor: hand;
    -fx-padding: 8 25;      /* Padding ajustado */
    -fx-background-radius: 20; /* Bordas muito arredondadas */
    -fx-effect: dropshadow(three-pass-box, rgba(123, 44, 191, 0.3), 5, 0, 0, 2);
}

.custom-button:hover {
    -fx-background-color: #7b2cbf;
    -fx-text-fill: white;
    -fx-effect: dropshadow(three-pass-box, rgba(123, 44, 191, 0.5), 8, 0, 0, 3);
}

.custom-button:pressed {
    -fx-background-color: #5a189a;
    -fx-effect: dropshadow(three-pass-box, rgba(123, 44, 191, 0.2), 3, 0, 0, 1);
}

/* Efeito de transição para os botões (controlado pelo Java) */
.button-with-animation {
    -fx-scale-x: 1.0;
    -fx-scale-y: 1.0;
    -fx-transition: all 0.2s ease;
}

.button-with-animation:hover {
    -fx-scale-x: 1.05;
    -fx-scale-y: 1.05;
}
