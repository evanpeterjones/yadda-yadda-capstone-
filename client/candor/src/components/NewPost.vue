<template>
  <div>
    <b-modal title="New Yapp"/>
  </div>
</template>

<script>
export default {
    name: "NewPost",
    mounted() {
        this.$children[0].show();
    }
}
</script>

<style>

</style>