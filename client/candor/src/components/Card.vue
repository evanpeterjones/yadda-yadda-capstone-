<template>
    <div>
        <div v-if="mobile" class="card">
            <slot></slot>
        </div>
        <div v-else class="card card-desktop" style="margin:auto;width: 50%;">
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    name: "Card",
    computed: {
        mobile : function() {
            return this.$store.getters.isMobile;
        }
    }
}
</script>

<style>
.card-desktop {
    padding: 20px;
}

.card {    
    box-shadow: 0 4px 8px 0 rgba(0,0,0,.2);
    transition: 0.3s;
}

.card:hover {
  box-shadow: 0 8px 16px 0 rgba(255,255,255,.2);
}

.container {
    padding: 2px 16px;
    border-radius: 10px;
}
</style>