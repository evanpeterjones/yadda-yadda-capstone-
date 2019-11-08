<template>
  <card>
    <div>
      <post-header
        :userId="user"
        :time="time" />
      <p> {{ content }} </p>
      <post-footer
        :decentral="decentral"
        :user="user"
        :id="id"
        :edited="edited" />
    </div>
  </card>
</template>

<script>
import PostHeader from './PostHeader.vue';
import PostFooter from './PostFooter'
import Card from './Card.vue'

export default {
    name: "Post",
    components: {
      Card, PostHeader, PostFooter
    },
    props: {
      id: {
        type: Number,
        default: null
      },
      user: {
        type: Number,
        default: null
      },
      time: {
        type: String,
        default: null
      },
      edited: {
        type: String,
        default: null
      },
      content: {
        type: String,
        default: null
      },
      decentral: {
        type: Boolean,
        default: false
      }
    }
}
</script>

<style>
post {
  color: white;
  border-radius: 10px;
}

p {
  padding-left: 5%;
  text-align: left;
}
</style>
