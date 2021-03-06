package net.blay09.mods.excompressum.compat.top;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.block.BlockWoodenCrucible;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.blay09.mods.excompressum.tile.TileBait;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class TheOneProbeAddon implements Function<ITheOneProbe, Void> {

	@Nullable
	@Override
	public Void apply(@Nullable ITheOneProbe top) {
		if(top != null) {
			top.registerProvider(new ProbeInfoProvider());
		}
		return null;
	}

	public static class ProbeInfoProvider implements IProbeInfoProvider {
		@Override
		public String getID() {
			return ExCompressum.MOD_ID;
		}

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state, IProbeHitData data) {
			// NOTE It seems like TheOneProbe does not have localization support o_O
			if(state.getBlock() instanceof BlockAutoSieveBase) {
				TileAutoSieveBase tileEntity = tryGetTileEntity(world, data.getPos(), TileAutoSieveBase.class);
				if(tileEntity != null) {
					addAutoSieveInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof BlockBait) {
				TileBait tileEntity = tryGetTileEntity(world, data.getPos(), TileBait.class);
				if(tileEntity != null) {
					addBaitInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof BlockWoodenCrucible) {
				TileWoodenCrucible tileEntity = tryGetTileEntity(world, data.getPos(), TileWoodenCrucible.class);
				if(tileEntity != null) {
					addWoodenCrucibleInfo(tileEntity, mode, info);
				}
			} else if(state.getBlock() instanceof BlockHeavySieve) {
				TileHeavySieve tileEntity = tryGetTileEntity(world, data.getPos(), TileHeavySieve.class);
				if(tileEntity != null) {
					addHeavySieveInfo(tileEntity, mode, info);
				}
			}
		}

		private void addAutoSieveInfo(TileAutoSieveBase tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getCustomSkin() != null) {
				info.text(String.format("Skin: %s", tileEntity.getCustomSkin().getName()));
			}
			if(tileEntity.getFoodBoost() > 1f) {
				info.text(String.format("Speed Boost: %.1f", tileEntity.getFoodBoost()));
			}
			if(tileEntity.getEffectiveLuck() > 1f) {
				info.text(String.format("Luck Bonus: %.1f", tileEntity.getEffectiveLuck() - 1));
			}
		}

		private void addBaitInfo(TileBait tileEntity, ProbeMode mode, IProbeInfo info) {
			TileBait.EnvironmentalCondition environmentalStatus = tileEntity.checkSpawnConditions(true);
			if(environmentalStatus == TileBait.EnvironmentalCondition.CanSpawn) {
				info.text("You are too close.");
				info.text("The animals are scared away.");
			} else {
				info.text("Unable to lure animals. Right-click for more info.");
				// list.add(TextFormatting.RED + environmentalStatus.langKey); // not doing this until we get localization support
			}
		}

		private void addWoodenCrucibleInfo(TileWoodenCrucible tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getSolidVolume() > 0f) {
				info.text(String.format("Solid Volume: %d", tileEntity.getSolidVolume()));
			}
			if(tileEntity.getFluidTank().getFluidAmount() > 0f) {
				info.text(String.format("Fluid Volume: %d", tileEntity.getFluidTank().getFluidAmount()));
			}
		}

		private void addHeavySieveInfo(TileHeavySieve tileEntity, ProbeMode mode, IProbeInfo info) {
			if(tileEntity.getProgress() > 0f) {
				info.progress((int) (tileEntity.getProgress() * 100), 100); // because a simple progress(float) isn't cool enough ..
			}
			ItemStack meshStack = tileEntity.getMeshStack();
			if (!meshStack.isEmpty()) {
				if(ExRegistro.doMeshesHaveDurability()) {
					info.text(String.format("%s %d/%d", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getItemDamage(), meshStack.getMaxDamage()));
				} else {
					info.text(meshStack.getDisplayName());
				}
			} else {
				info.text("No Mesh");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private static <T extends TileEntity> T tryGetTileEntity(World world, BlockPos pos, Class<T> tileClass) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null && tileClass.isAssignableFrom(tileEntity.getClass())) {
			return (T) tileEntity;
		}
		return null;
	}

}
