package sus.keiger.plugincommon.entity;

import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public interface ILivingEntityWrapper extends IEntityWrapper
{
    boolean AddPotionEffect(PotionEffect effect);
    boolean ClearPotionEffects();
    List<PotionEffect> GetPotionEffects();
    void RemovePotionEffect(PotionEffectType type);
    boolean HasPotionEffect(PotionEffectType type);
    PotionEffect GetPotionEffect(PotionEffectType type);

    void MeleeAttack(Entity target);
    boolean IsAbleToBreatherUnderwater();
    void InterruptActiveItemUsage();
    void FinishActiveItemUsage();
    ItemStack GetActiveItem();
    EntityEquipment GetEquipment();
    int GetMaximumAir();
    void SetMaximumAir(int value);
    int GetRemainingAir();
    void SetRemainingAir(int value);
    boolean IsClimbing();
    boolean IsGliding();
    void SetIsGliding(boolean value);
    boolean IsCollidable();
    boolean IsRiptiding();
    boolean IsSwimming();
    void SetIsSwimming(boolean value);
    boolean IsAIEnabled();
    void SetIsAIEnabled(boolean value);
    boolean IsAbleToPickUpItems();
    void SetIsAbleToPickUpItems(boolean value);
    boolean IsJumping();
    void SetIsJumping(boolean value);
    Entity GetLeashHolder();
    boolean SetLeashHolder();
}