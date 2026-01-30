package dirogue.example.rencontre;

import dirogue.example.code_squelette.*;

public class Boss extends Gargouille{
    @Override
    public String rencontre() {
        return "La bataille finale!";
    }

    @Override
    public RencontreType essence() {
        return RencontreType.BOSS;
    }
}
