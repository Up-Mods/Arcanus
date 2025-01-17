Arcanus is a small magic mod that allows players to create their own spells, and a casting system akin to Wynncraft. Find a Wizard Tower, don a hat, and speak to the wizard at the top to start your magical journey.

![](https://mod-assets.upcraft.dev/promo/arcanus-continuum/wizard_tower.png)

---

To create a spell, you first need to make a spell book and place it on a lectern. Depending on how many knowledge scrolls you've obtained from your local Wizard, you will see a variety of spell shapes on the left and spell effects on the right. These shapes and effects can be chained together in order to create fully customizable spells, with the only limits on their power being their mana cost and your own creativity.

![](https://mod-assets.upcraft.dev/promo/arcanus-continuum/spellcrafting.png)

Spell shapes are how the effects are delivered to the target. For instance, a self shape will apply the chained effects to the caster, while a projectile shape will apply the chained effects to whatever it hits. Spell effects are more obvious; they are the actual functioning parts of the spell, such as damage or freezing.

---

In order to cast a spell you have to combine mouse inputs that correspond to one of 8 slots on your staff. Each mouse input also corresponds to a shape: square for left, circle for right. When you cast a spell, you'll see these shapes appear around your crosshair, and other players will see them in front of your player model.

<!-- update the cache value with the latest time from https://www.unixtimestamp.com to force a cache refresh -->
![](https://mod-assets.upcraft.dev/promo/arcanus-continuum/modpage_banner.png?cache=1736511913)

---

The mod is fully documented in the in-game book, the Compedium Arcanus. I highly recommend reading through it to understand everything there is to know about the mod! It's also fully configurable, including allowing pack makers to disable specific spell shapes or spell effects completely.

## For Developers:

You can also find the mod on Up's [maven repository](https://maven.uuid.gg/#/releases).

```gradle
repositories {
	maven { url = "https://maven.uuid.gg/releases" }
}

dependencies {
	modImplementation "dev.cammiescorner.arcanuscontinuum:Arcanus-Continuum-Fabric:<VERSION>"
}
```

---

<p align="center">
	<a href="https://cammiescorner.dev/discord"><img src="https://cammiescorner.dev/images/extras/discord.png" width="150" height="150" title="Join my Discord" alt="The Discord icon, which is a blue circle with half in shadow. In the middle is a weird little robot-helmet-looking-thingy."></a>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="https://www.ko-fi.com/camellias"><img src="https://cammiescorner.dev/images/extras/kofi.png" width="150" height="150" title="Support me on Ko-Fi" alt="The Ko-Fi logo, a sky-blue circle with a white coffee mug with a red heart on it in the middle."></a>
</p>
