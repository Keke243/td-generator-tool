package dirogue.example.rencontre;

import dirogue.example.code_squelette.RencontreType;

public class SacDeButin extends Tresor{

    @Override
    public String rencontre() {
        return "Sac de Butin! Quelle chance!";
    }

    @Override
    public String getSprite() {
        return "src/main/resources/images/sample.png";
    }
    
    @Override
    public RencontreType essence() {
        return RencontreType.TRESOR;
    }
}
