<template>
  <card>
    <div>
      <post-header
        :reply-to="parent"
        :user-id="user"
        :time="time"
      />
      <div class="content">
        <p> {{ content }} </p>
      </div>
      <post-footer
        :id="id"
        :has-comments="hasComments"
        :decentral="decentral"
        :user="user"
        :edited="edited"
      />
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
      }, 
      parent: {
        type: Number, 
        default: null
      },
      hasComments: {
        type: Boolean,
        default: null
      }
    }
}
</script>

<style>
p {
  padding-left: 5%;
  text-align: left;
}
.content {
  padding-right: 20px;
}
</style>
