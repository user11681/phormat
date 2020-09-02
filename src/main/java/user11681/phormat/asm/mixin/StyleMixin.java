package user11681.phormat.asm.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import user11681.phormat.asm.access.ExtendedStyle;
import user11681.phormat.asm.access.PhormatAccess;

@Mixin(Style.class)
abstract class StyleMixin implements ExtendedStyle {
    @Unique
    private final ReferenceOpenHashSet<PhormatAccess> phormattings = new ReferenceOpenHashSet<>(new PhormatAccess[5], 0, 0, 0.75F);

    @Unique
    private Formatting switchFormatting;

    @Unique
    private Boolean previousObfuscated;

    @Override
    @Unique
    public boolean hasPhormatting(final PhormatAccess formatting) {
        return this.phormattings.contains(formatting);
    }

    @Override
    @Unique
    public boolean hasPhormatting(final Formatting formatting) {
        return this.phormattings.contains(formatting);
    }

    @Override
    @Unique
    public ReferenceOpenHashSet<PhormatAccess> getPhormattings() {
        return this.phormattings;
    }

    @Override
    @Unique
    public void transferPhormats(final Style to) {
        this.transferPhormats(((StyleMixin) (Object) to));
    }

    @Unique
    private void transferPhormats(final StyleMixin style) {
        style.addPhormattings(this.phormattings);
    }

    @Override
    @Unique
    public void addFormattings(final Collection<Formatting> formattings) {
        final Iterator<Formatting> iterator = formattings.iterator();

        while (iterator.hasNext()) {
            this.addPhormatting(iterator.next());
        }
    }

    @Override
    @Unique
    public void addPhormattings(final Collection<PhormatAccess> formattings) {
        final Iterator<PhormatAccess> iterator = formattings.iterator();

        while (iterator.hasNext()) {
            this.addPhormatting(iterator.next());
        }
    }

    @Override
    @Unique
    public void addPhormattings(final Formatting... formattings) {
        for (final Formatting formatting : formattings) {
            this.addPhormatting((PhormatAccess) (Object) formatting);
        }
    }

    @Override
    @Unique
    public void addPhormattings(final PhormatAccess... formattings) {
        for (final PhormatAccess formatting : formattings) {
            this.addPhormatting(formatting);
        }
    }

    @Override
    @Unique
    public void addPhormatting(final Formatting formatting) {
        this.addPhormatting((PhormatAccess) (Object) formatting);
    }

    @Override
    @Unique
    public void addPhormatting(final PhormatAccess format) {
        this.phormattings.add(format);
    }

    @Override
    @Unique
    public Style cast() {
        return (Style) (Object) this;
    }

    @Inject(method = "withColor(Lnet/minecraft/text/TextColor;)Lnet/minecraft/text/Style;",
            at = @At("RETURN"))
    public void withColor(final TextColor color, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = "withBold",
            at = @At("RETURN"))
    public void withBold(final Boolean bold, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = "withItalic",
            at = @At("RETURN"))
    public void withItalic(final Boolean italic, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "method_30938",
            at = @At("RETURN"))
    public void method_30938(final Boolean boolean_, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = "withClickEvent",
            at = @At("RETURN"))
    public void withClickEvent(final ClickEvent clickEvent, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = "withHoverEvent",
            at = @At("RETURN"))
    public void withHoverEvent(final HoverEvent hoverEvent, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = "withInsertion",
            at = @At("RETURN"))
    public void withInsertion(final String insertion, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "withFont",
            at = @At("RETURN"))
    public void withFont(final Identifier font, final CallbackInfoReturnable<Style> info) {
        this.transferPhormats(info.getReturnValue());
    }

    @Inject(method = {"withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", "withExclusiveFormatting"},
            at = @At(value = "TAIL"))
    public void addPhormatting(final Formatting formatting, final CallbackInfoReturnable<Style> info) {
        final StyleMixin style = (StyleMixin) (Object) info.getReturnValue();

        this.transferPhormats(style);

        if (((PhormatAccess) (Object) formatting).isCustom()) {
            style.addPhormatting(formatting);
        }
    }

    @Redirect(method = {"withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", "withExclusiveFormatting", "withFormatting([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;"},
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/util/Formatting;ordinal()I"))
    public int hackOrdinal(final Formatting formatting) {
        return ((PhormatAccess) (Object) (this.switchFormatting = formatting)).isCustom() && !formatting.isColor() ? Formatting.OBFUSCATED.ordinal() : formatting.ordinal();
    }

    @ModifyVariable(method = {"withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", "withExclusiveFormatting", "withFormatting([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;"},
                    at = @At(value = "CONSTANT",
                             args = "intValue=1",
                             ordinal = 0),
                    ordinal = 4)
    public Boolean saveObfuscated(final Boolean previous) {
        return this.previousObfuscated = previous;
    }

    @ModifyVariable(method = {"withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", "withExclusiveFormatting", "withFormatting([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;"},
                    at = @At(value = "STORE",
                             ordinal = 1),
                    ordinal = 4)
    public Boolean fixObfuscatedVarargs(final Boolean previous) {
        return this.switchFormatting == Formatting.OBFUSCATED || this.previousObfuscated == Boolean.TRUE;
    }

    @Inject(method = "withFormatting([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;",
            at = @At(value = "TAIL"))
    public void addPhormatting(final Formatting[] formattings, final CallbackInfoReturnable<Style> info) {
        final StyleMixin style = (StyleMixin) (Object) info.getReturnValue();

        for (final PhormatAccess format : (PhormatAccess[]) (Object) formattings) {
            if (format.isCustom()) {
                style.addPhormatting(format);
            }
        }
    }

    @Inject(method = "withParent", at = @At("TAIL"))
    public void withParent(final Style parent, final CallbackInfoReturnable<Style> info) {
        if (parent != Style.EMPTY) {
            final StyleMixin child = (StyleMixin) (Object) info.getReturnValue();

            for (final PhormatAccess phormatting : ((StyleMixin) (Object) parent).phormattings) {
                if (!child.hasPhormatting(phormatting)) {
                    child.addPhormatting(phormatting);
                }
            }
        }
    }

    @Inject(method = "toString",
            at = @At("RETURN"),
            cancellable = true)
    public void appendPhormattings(final CallbackInfoReturnable<String> info) {
        final String string = info.getReturnValue();

        info.setReturnValue(string.substring(0, string.lastIndexOf('}')) + ", phormattings=" + Arrays.toString(this.phormattings.toArray(new PhormatAccess[0])) + '}');
    }

    @Inject(method = "equals",
            at = @At(value = "RETURN",
                     ordinal = 1),
            cancellable = true)
    public void comparePhormattings(final Object obj, final CallbackInfoReturnable<Boolean> info) {
        final ReferenceOpenHashSet<PhormatAccess> otherPhormattings = ((StyleMixin) obj).phormattings;
        final ReferenceOpenHashSet<PhormatAccess> phormattings = this.phormattings;

        if (otherPhormattings.size() != phormattings.size()) {
            info.setReturnValue(false);
        }

        for (final PhormatAccess phormat : otherPhormattings) {
            if (!phormattings.contains(phormat)) {
                info.setReturnValue(false);

                return;
            }
        }
    }

    @ModifyArg(method = "hashCode",
               at = @At(value = "INVOKE",
                        target = "Ljava/util/Objects;hash([Ljava/lang/Object;)I"))
    public Object[] hashPhormattings(final Object[] previous) {
        final Object[] phormattings = this.phormattings.toArray();
        final int previousLength = previous.length;
        final int formattingCount = phormattings.length;
        final Object[] all = Arrays.copyOf(previous, previousLength + formattingCount);

        System.arraycopy(phormattings, 0, all, previousLength, formattingCount);

        return all;
    }

    @Mixin(Style.Serializer.class)
    abstract static class SerializerMixin {
        @Inject(method = "deserialize",
                at = @At(value = "NEW",
                         target = "net/minecraft/text/Style"))
        public void deserializeCustomFormatting(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext, final CallbackInfoReturnable<Style> info) {
            final ExtendedStyle style = (ExtendedStyle) info.getReturnValue();
            final JsonObject object = jsonElement.getAsJsonObject();

            if (object.has("phormat")) {
                for (final JsonElement phormat : object.getAsJsonArray("phormat")) {
                    style.addPhormatting(Formatting.byName(phormat.getAsString()));
                }
            }
        }

        @Inject(method = "serialize",
                at = @At("RETURN"),
                cancellable = true)
        public void serializeCustomFormatting(final Style style, final Type type, final JsonSerializationContext jsonSerializationContext, final CallbackInfoReturnable<JsonElement> info) {
            JsonObject object = (JsonObject) info.getReturnValue();

            if (object == null) {
                info.setReturnValue(object = new JsonObject());
            }

            final JsonArray phormattings = new JsonArray();

            for (final PhormatAccess phormat : ((ExtendedStyle) style).getPhormattings()) {
                if (phormat.isCustom()) {
                    phormattings.add(phormat.cast().getName());
                }
            }

            object.add("phormat", phormattings);
        }
    }
}
