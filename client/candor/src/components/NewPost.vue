<template>
  <div>
    <b-modal 
      hide-header
      prevent-defaults
      @hidden="close"
      @ok="send(textInput)"
      ok>
      <b-form-textarea
        id="New Post"   
        v-model="textInput"
        placeholder="What's happening?"
        rows="4"></b-form-textarea>
      <template v-slot:modal-footer="{ ok, cancel, hide }">
        <div v-if="validPost">
          <b style="color:grey">{{ charsLeft }}</b>
        </div>
        <div v-else>
          <b style="color:red">{{ charsLeft }}</b>
        </div>
        <b-button pill button-size='lg' @click="cancel()">
          ❌
        </b-button>
        <b-button pill variant="primary" button-size='lg' @click="ok()">
          📣
        </b-button>        
      </template>
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
    computed: {
      charsLeft: function() {
        return (this.textInput.length) + "/140"
      }, 
      validPost: function() {
        return this.textInput.length <= 140
      }
    },
    methods: {
      close : function() {
        console.log('clicked close');
        this.$emit('closeDialog');
      }, 
      send: function(data) {
        if (!data) { console.log("data can't be null"); return; }        
        if (data.length > 140) { alert("post must be shorter than 140 characters") }

        this.$http.get("/newPost", {
          params: {
            content : (data) ? data : ""
          }
        }).then(response => {
          console.log(response)
          this.$store.commit("newPost", response.data[0])
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