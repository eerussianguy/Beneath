package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.misc.BeneathEntityTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.common.entities.TFCEntities;

public class BuiltinEntityTags extends TagsProvider<EntityType<?>> implements Accessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    public BuiltinEntityTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(event.getGenerator().getPackOutput(), Registries.ENTITY_TYPE, lookup, Beneath.MOD_ID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BeneathEntityTags.CAN_BE_SACRIFICED)
            .add(TFCEntities.GOAT.key())
            .add(TFCEntities.PIG.key())
            .add(TFCEntities.SHEEP.key());
        BeneathEntities.BOATS.values().forEach(boat -> tag(Tags.EntityTypes.BOATS).add(boat.key()));
        BeneathEntities.CHEST_BOATS.values().forEach(boat -> tag(Tags.EntityTypes.BOATS).add(boat.key()));
    }
}
