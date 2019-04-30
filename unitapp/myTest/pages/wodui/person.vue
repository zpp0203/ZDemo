<template>
	<view class="content">
		<image class="logo" src="/static/logo.png"></image>
		<view>
			<text class="title">{{title}}</text>
		</view>
		<!-- #ifdef APP-PLUS -->
		<button type="primary" @click="openBarcode">扫码</button>
		<!-- #endif -->
	</view>
</template>

<script>
	let showing = false;
	export default {
		data() {
			return {
				title: 'Hello'
			};
		},
		methods: {
			// #ifdef APP-PLUS
			openBarcode() {
				
// 				uni.scanCode({
// 					onlyFromCamera: true,// 只允许通过相机扫码
// 					success: function (res) {
// 						console.log('条码类型：' + res.scanType);
// 						console.log('条码内容：' + res.result);
// 					}
// 				});
				if (showing) {
					return;
				}
				showing = true;
				var _self = this;
				var barcodeWebview = plus.webview.create('_www/hybrid/html/barcode2.html', 'barcode', {
					//webview的标题栏
					titleNView: {
						autoBackButton: true,
						backgroundColor: '#FF0000',
						titleColor: '#FFFFFF',
						titleText: '自定义扫码界面'
					}
				});
				barcodeWebview.addEventListener('close', function() {
					var barcodeValue = plus.storage.getItem('barcode_value');
					if (barcodeValue) {
						var barcodeResult = JSON.parse(barcodeValue);
						if (barcodeResult.code === 0) {
							_self.title = '扫码结果：' + barcodeResult.result;
						} else {
							_self.title = '扫码失败';
						}
					}
				});
				barcodeWebview.show('slide-in-right', 200, function() {
					showing = false;
				});
			},
			// #endif
		}
	}
</script>

<style>
	.content {
		text-align: center;
		height: 400upx;
	}
	.logo {
		height: 200upx;
		width: 200upx;
		margin-top: 200upx;
	}
	.title {
		font-size: 36upx;
		color: #8f8f94;
		word-break: break-all;
	}
</style>
