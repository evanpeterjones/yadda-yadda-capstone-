<template>
  <div class="container"> 
    <div class="row">
      <p>{{ userName }}</p>
      <div class="col-xl">
        {{ }}
      </div>
      <div class="col-xl-2">
        {{ parsedTime }}
      </div>
    </div>
  </div>
</template>

<script>
import getName from '../utils/name-service.js'
import getTime from '../utils/time-service.js'

export default {
  name: 'PostHeader',
  props: {
    userId: { type: String, default: null },
    time: { type: String, default: '' }
  }, 
  computed: {
    'userName': function() {
      if (this.userId) {
        return getName(this.userId);
      }

      return 'Purple Otter';
    }, 
    'parsedTime': function() {
      if (this.time) {
        return getTime(this.time);
      }

      return '0:00';
    }
  }
}
</script>

<style>
.container {
  margin: 5px;
  color: grey;
}
</style>