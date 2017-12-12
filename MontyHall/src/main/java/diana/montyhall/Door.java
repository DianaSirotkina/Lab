package diana.montyhall;

/**
 * A door representation, holding the door's id.
 */
class Door
{    
    private final int id;
    
    Door(int id)
    {
        this.id = id;
    }
    
    int getId()
    {
        return this.id;
    }
}
