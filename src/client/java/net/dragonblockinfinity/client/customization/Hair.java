package net.dragonblockinfinity.client.customization;

/**
 * Hair — estilos de cabelo selecionaveis na tela de customizacao
 * (CaracterScreen). Por enquanto so existe HAIR_1 (Hair1Mesh/Hair1Layer);
 * novos estilos entram aqui como novas constantes no futuro.
 *
 * Java puro (sem imports Minecraft): so guarda o nome de exibicao e
 * a navegacao ciclica (proxima/anterior).
 */
public enum Hair {
    HAIR_1("Hair 1");

    private final String displayName;

    Hair(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Retorna o proximo estilo de cabelo, voltando para o primeiro apos o ultimo. */
    public Hair next() {
        Hair[] values = values();
        int nextIndex = (this.ordinal() + 1) % values.length;
        return values[nextIndex];
    }

    /** Retorna o estilo de cabelo anterior, voltando para o ultimo antes do primeiro. */
    public Hair previous() {
        Hair[] values = values();
        int previousIndex = (this.ordinal() - 1 + values.length) % values.length;
        return values[previousIndex];
    }
}
