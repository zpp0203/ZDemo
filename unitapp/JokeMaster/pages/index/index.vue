<template>
	<view class="content">
		<view class="content-text">
			<text class="title">{{content}}</text>
		</view>
		
		<view>
			<text class="action action-up" @click="loadContext(1)">上一条</text>
			<text class="action action-ub" @click="loadContext(2)">下一条</text>
		</view>
	</view>
</template>

<script>
	export default {
		
		data() {
			return {
				datas:{
					reason:'',
					result:[]
				} ,
				
				content: '',
				count:0,
				showCount:0
			}
		},
		onReady() {
			this.loadContext(0);
		},
		methods: {
			loadContext(position){
				if(this.showCount<this.count){
			
					if(position==1){
						if(this.showCount>1)
							this.showCount--;
					}else{
						this.showCount++;
					}
					this.content=this.datas.result[this.showCount].content;
	
					return;
				}
				
				uni.request({
				    url: 'https://v.juhe.cn/joke/randJoke.php?', //仅为示例，并非真实接口地址。
				    data: {
				        key: '968722fcec0c73f0635f921f860de946',
						pagesize:1
				    },
				    success: (res) => {
						console.log(res.data);
						
						this.datas=res.data;
						this.count=this.datas.result.length;
				    
						if(this.datas.result.length>0){
							this.content=this.datas.result[0].content;
							this.showCount=0;
						}
							
				    }
				});
			},
			
		}
	}
</script>

<style>
	.content {
		height: 100vh;
		align-items: center;
		justify-content: center;
	}

	.content-text {
		display: flex;
		height: 70vh;
		justify-content: center;
		align-items: center;
	}

	.title {
		font-size: 36rpx;
		color: #000000;
		padding: 10rpx;
	}
	
	.action{
		background-color: #aaff7f;
		color: #007AFF;
		padding: 10rpx;
	}
	.action-up {
		float: left;
		margin-left: 30rpx;
	}
	.action-ub {
		float: right;
		margin-right: 30rpx;
	}
</style>
