package dirogue.example.rencontre;

import dirogue.example.code_squelette.RencontreType;

public class Rien extends Rencontre {

    @Override
    public String rencontre() {
        return "Un moment pacifique.";
    }

    @Override
    public String getSprite() {
        return "src/main/resources/images/sample.png";
    }
    
    @Override
    public RencontreType essence() {
        return RencontreType.RIEN;
    }
}
