package sus.keiger.plugincommon;

public interface ITickExecutor
{
    void AddTickable(ITickable tickable);
    void RemoveTickable(ITickable tickable);
}