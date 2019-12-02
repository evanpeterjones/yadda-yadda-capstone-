<template>
  <div class="container"> 
    <div class="row">
      <div class="col font-weight-bold text-nowrap">
        <font-awesome-icon
          v-if="replyTo"
          :icon="['fa', 'reply']"
          @click="getComments"
        />
        <font-awesome-icon
          v-else
          :icon="['fa', 'atom']"
        />
        {{ userName }}
      </div>
      <div class="col" />
      <div class="col text-nowrap">
        <p>{{ parsedTime }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import getName from '../utils/name-service.js'
import time from '../utils/time-service.js'

export default {
  name: 'PostHeader',
  props: {
    userId: { type: Number, default: -1 },
    time: { type: String, default: '' },
    replyTo: { type:Number, default: null }
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
        return time.getTimeSince(this.time);
      }

      return '0:00';
    }
  },
  methods: {
    getComments: function() {
      this.$http.get('/postComments', {
          params: {
            post: this.replyTo
          }
        }).then(response => {
          console.log(response)
          this.$store.commit('addNewCommentsFeed', response.data)
        }).catch(error => {
          console.log("yeet")
          console.error(error);
        })
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