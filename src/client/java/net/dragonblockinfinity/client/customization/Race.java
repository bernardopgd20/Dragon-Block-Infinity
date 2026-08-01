package net.dragonblockinfinity.client.customization;

/**
 * Race — racas selecionaveis na tela de customizacao (CaracterScreen).
 *
 * Java puro (sem imports Minecraft): so guarda o nome de exibicao e
 * a navegacao ciclica (proxima/anterior), que e o que as setas
 * esquerda/direita do CaracterScreen usam.
 */
public enum Race {
    SAYAJIN("Sayajin"),
    HUMANO("Humano");

    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Retorna a proxima raca na lista, voltando para a primeira apos a ultima. */
    public Race next() {
        Race[] values = values();
        int nextIndex = (this.ordinal() + 1) % values.length;
        return values[nextIndex];
    }

    /** Retorna a raca anterior na lista, voltando para a ultima antes da primeira. */
    public Race previous() {
        Race[] values = values();
        int previousIndex = (this.ordinal() - 1 + values.length) % values.length;
        return values[previousIndex];
    }
}
