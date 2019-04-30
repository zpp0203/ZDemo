<template>
	<content class='gradient-back'>
		<view class="top">
			<view class="other" @click="toOther">其他</view>
			<view class="other register">注册</view>
		</view>
		
		<image class="circular head_img" src="../../static/logo.png"></image>
		
		<input v-model="user" class="login-input login-input-user" type="number" maxlength="11" placeholder="请输入手机号" placeholder-style="color:#FFFFFF"/>
		<input v-model="psw" class="login-input login-input-psw" type="text" password="true" maxlength="18" placeholder="请输入密码" placeholder-style="color:#FFFFFF"/>
		
		<button class="login-input login-button" @tap="login">登入</button>
		
		<view class="login-forget-psw">  ——  忘记密码  ——  </view>
	</content>
</template>

<script>
	export default {
		data() {
			return {
				user:"",
				psw:""
			};
		},
		methods:{
			toOther(){
				console.debug("点击other")
				uni.reLaunch({
					url:"../test/test"
				})
			},
			login(){
				if (this.user.length>=11 && this.psw.length) {
					console.debug(this.user+" 登入 "+this.psw)
					uni.reLaunch({
						url:"home"
					})
				} else{
					uni.showToast({
						title:'请输入完整',
						icon:'none'
					})
				}
				
				
			}
		}
	}
</script>

<style>
	@import '../../common/zpp.css';
	.top{

	}
	.other{
		padding: 20upx;
		font-size: 32upx;
		color: #FFFFFF;
		float: left;
	}
	.register{
		float: right;
	}
	.head_img{
		height: 345upx;
		width: 345upx;
		position:absolute;
		left:50%;
		margin-left:-172.5upx;
		margin-top: 175upx;
	}
	.login-input{
		width: 390upx;
		height: 80upx;
		align-items: center;
		text-align: center;
		position:absolute;
		left:50%;
		margin-left:-172.5upx;
		font-size: 32upx;
		color: #FFFFFF;
		border:solid 1upx #FFFFFF;
		border-radius: 40upx;
	}
	.login-input-user{
		margin-top: 670upx;
	}
	.login-input-psw{
		margin-top: 785upx;
	}
	.login-button{
		border: none;
		background: #FFFFFF;
		color: #000000;
		margin-top: 938upx;
	}
	.login-forget-psw{
		width: 100%;
		text-align: center;
		margin-top: 1130upx;
		color: #FFFFFF;
	}
</style>
