package sus.keiger.plugincommon;

public interface ITickExecutor extends ITickable
{
    void AddTickable(ITickable tickable);
    void RemoveTickable(ITickable tickable);
    void ClearTickables();
}