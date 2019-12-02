<template>
    <div>
        <b-modal 
            size='lg'
            title='My Posts'
            hide-footer
            @hidden="hide">
            <div v-if="!feed">
                <p> You have no posts </p>
            </div>
            <div v-else v-for="post in feed" :key="post.pst_id_pk">
                <post 
                    :hasComments="post['pst_hascomments']"
                    :parent="post['pst_parent_fk']"
                    :id="post['pst_id_pk']"
                    :user="post['pst_usr_id_fk']"
                    :time="post['pst_time']"
                    :edited="post['pst_edittime']"
                    :content="post['pst_content']"
                    :decentral="post['pst_decentral']" />
            </div>
        </b-modal>
    </div>    
</template>

<script>
import Post from './Post'

export default {
    name: "MyPosts", 
    components: {
        Post
    },
    data() {
        return { 
            feed : null
        }
    },
    mounted() {
        this.$children[0].show();

        this.$http.get("/myPosts").then((result) => {
            console.log(result.data)
            this.feed = (result.data.length == 0) ? 
                            null : result.data;
        }).catch(result => {
            console.log(result);
        });
    },
    methods: {
        hide: function() {
            this.$emit('closeDialog');
        }
    }
}
</script>
