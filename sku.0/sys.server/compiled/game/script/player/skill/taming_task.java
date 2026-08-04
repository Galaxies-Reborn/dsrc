package script.player.skill;

/**
 * Attached message receiver for the asynchronous pre-CU taming lifecycle.
 * Command-table handlers are dispatch targets, not persistent message targets,
 * so cmdTame owns this task script only until the lifecycle is cleared.
 */
public class taming_task extends script.player.skill.taming
{
    public taming_task()
    {
    }
}
