<template>
  <div class="container"> 
    <div class="row">
      <div class="col font-weight-bold text-nowrap">
        <font-awesome-icon :icon="['fa', 'atom']"></font-awesome-icon>
        {{ userName }}
      </div>
      <div class="col"></div>
      <div class="col text-nowrap">
        <p>{{ parsedTime }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import getName from '../utils/name-service.js'
import getTimeSince from '../utils/time-service.js'

export default {
  name: 'PostHeader',
  props: {
    userId: { type: Number, default: -1 },
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
        return getTimeSince(this.time);
      }

      return '0:00';
    }
  }
}
</script>

<style>
.container {
  color: grey;
  margin: 5px;
}
</style>