package dirogue.example.rencontre;

import dirogue.example.code_squelette.RencontreType;

public class Gobelin extends Monstre {

    @Override
    public String rencontre() {
        return "Un Gobelin affreux!";
    }

    @Override
    public String getSprite() {
        return "src/main/resources/images/sample.png";
    }
    
    @Override
    public RencontreType essence() {
        return RencontreType.MONSTRE;
    }
}
