package shared.model.card;

import shared.CardTypes;

public class SpellCard extends Card{
    public SpellCard(CardTypes type , int cost , int damage , String walkFrmPath ,
                     int walkFrmNum, String attackFrmPath, int attackFrmNum, String dieFrmPath , int dieFrmNum)
    {
        super(type, cost, damage, walkFrmPath, walkFrmNum, attackFrmPath, attackFrmNum, dieFrmPath, dieFrmNum);

    }
}
