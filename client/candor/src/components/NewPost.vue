<template>
  <div>
    <b-modal 
      prevent-defaults
      title="New Yapp"
      @hidden="close"
      @ok="send(textInput)"
      ok>
      <b-form-textarea
        id="New Post"   
        v-model="textInput"
        placeholder="What's happening?"
        rows="4"></b-form-textarea>
    </b-modal>
  </div>
</template>

<script>
export default {
    name: "NewPost",
    data() {
      return {
        textInput : ""
      }
    },
    methods: {
      close : function() {
        console.log('clicked close');
        this.$emit('closeDialog');
      }, 
      send: function(data) {
        if (!data) { console.log("data can't be null"); return; }

        this.$http.post("/newPost", null, {
          params: {
              content : (data) ? data : ""
          }
        }).then(response => {
          console.log(response)
          console.log('Yay, new post!!!');
        }).catch(error => {
          console.error(error)
        });
      }
    },
    mounted() {
        this.$children[0].show();
    }
}
</script>

<style>

</style>