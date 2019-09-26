<template>
  <div class="container"> 
    <div class="row">
      <div class="col-xl">
        {{ }}
      </div>
      <div class="col-xl">
        {{ }}
      </div>
      <div class="col-xl">
        {{ time }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
    name: 'PostHeader',
    props: {
        time: { type: String, default: '' }
    }
}
</script>

<style>

</style>