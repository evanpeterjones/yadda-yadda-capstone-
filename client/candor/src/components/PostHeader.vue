<template>
  <div class="container"> 
    <div class="row">
      <p>{{ userName }}</p>
      <p style="align:right;">{{ parsedTime }}</p>
    </div>
  </div>
</template>

<script>
import getName from '../utils/name-service.js'
import getTimeSince from '../utils/time-service.js'

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
        return getTimeSince(this.time);
      }

      return '0:00';
    }
  }
}
</script>

<style>
.container {
  width: 100%;
  margin: 5px;
  color: grey;
}
</style>