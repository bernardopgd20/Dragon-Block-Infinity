package net.dragonblockinfinity.client.customization;

/**
 * CharacterSelectionState — guarda a raca e o cabelo atualmente
 * selecionados pelo jogador local na tela de customizacao
 * (CaracterScreen).
 *
 * Estado estatico simples: vale so para o jogador local nesta sessao
 * (nao sincroniza com outros clientes, nao persiste entre sessoes).
 * Java puro (sem imports Minecraft) — Hair1Layer/o mixin de
 * renderizacao leem esses valores para decidir o que desenhar.
 */
public final class CharacterSelectionState {

    private static Race selectedRace = Race.SAYAJIN;
    private static Hair selectedHair = Hair.HAIR_1;

    private CharacterSelectionState() {
        // Classe utilitaria de estado estatico: nao deve ser instanciada.
    }

    public static Race getSelectedRace() {
        return selectedRace;
    }

    public static Hair getSelectedHair() {
        return selectedHair;
    }

    /** Avanca a raca selecionada para a proxima (seta direita). */
    public static void nextRace() {
        selectedRace = selectedRace.next();
    }

    /** Volta a raca selecionada para a anterior (seta esquerda). */
    public static void previousRace() {
        selectedRace = selectedRace.previous();
    }

    /** Avanca o cabelo selecionado para o proximo (seta direita). */
    public static void nextHair() {
        selectedHair = selectedHair.next();
    }

    /** Volta o cabelo selecionado para o anterior (seta esquerda). */
    public static void previousHair() {
        selectedHair = selectedHair.previous();
    }
}
