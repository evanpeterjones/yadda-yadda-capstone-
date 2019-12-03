<template>
    <div>
        <b-modal 
            title="My Account"
            prevent-defaults
            @ok="updateAccountInfo"
            @hidden="hide">
            <b-form>
                <b-form-group
                    label="User ID"
                    label-for="input-1">                
                    <b-form-input
                        disabled
                        id="input-1"
                        v-model="user_data.usr_id_pk"></b-form-input>
                </b-form-group>
                
                <b-form-group
                    label="Username"
                    label-for="input-1"
                    description="this isn't currently implemented elsewhere">
                    <b-form-input
                        id="input-1"
                        v-model="user_data.usr_username"></b-form-input>
                </b-form-group>

                <b-form-group
                    label="Email"
                    label-for="input-1"
                    description="We'll never share your email with anyone else.">
                    <b-form-input
                        id="input-1"
                        v-model="user_data.usr_email"
                        type="email"></b-form-input>
                </b-form-group>
                <b-alert
                    fade
                    dismissible
                    @dismissed="errorUpdating=false"
                    :show="errorUpdating">
                    This email is already registered
                </b-alert>


                <b-form-group
                    label="Account Created"
                    label-for="input-1">
                    <b-form-input
                        disabled
                        id="input-1"
                        v-model="user_data.usr_createdon"
                        type="date"></b-form-input>
                </b-form-group>
            </b-form>
        </b-modal>
    </div>    
</template>

<script>
import time from '../utils/time-service.js'

export default {
    name: "MyAccount", 
    data() {
        return { 
            user_data : [], 
            errorUpdating : false
        }
    },    
    methods: {
        setResult: function(data) {
            if (data.usr_pwd_fk) {
                delete data.usr_pwd_fk;
            }

            this.user_data = {
                ...data,
                usr_createdon: time.formatTime(data.usr_createdon)
            };
        },
        updateAccountInfo: function(evt) {
            evt.preventDefault();
            this.$http.get('updateAccountInfo', {
                params : {
                    id : this.user_data.usr_id_pk,
                    username : this.user_data.usr_username,
                    email : this.user_data.usr_email,
                }
            }).then(result => {
                this.setResult(result.data[0]);
                this.hide();
            }).catch(err => {
                this.errorUpdating = true;    
            });
        },
        hide: function() {
            this.$emit('closeDialog');
        }
    }, 
    mounted() {
        
        this.$http.get('userInformation').then(result => {
            this.setResult(result.data[0]);
        }).catch(error => {
            console.log(error);
        });

        this.$children[0].show();
        
    }
}
</script>

<style>

</style>