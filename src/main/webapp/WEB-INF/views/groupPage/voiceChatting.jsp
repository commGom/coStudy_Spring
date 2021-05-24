<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<link href="/resources/css/voiceChatting.css" rel="stylesheet"
	type="text/css">
<title>스터디 그룹 음성 채팅</title>
 <link
      href="vendor/fontawesome-free/css/all.min.css"
      rel="stylesheet"
      type="text/css"
    />
    <link
      href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
      rel="stylesheet"
    />
     <link href="/resources/css/sb-admin-2.min.css" rel="stylesheet" />
</head>
<body id="page-top">
	<!-- Page Wrapper -->
	<div id="wrapper">

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">
			<!-- Main Content -->
			<div id="content">
				<!-- Topbar -->
				<nav
					class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
				
				<h3 class="h3 mb-4 text-gray-800">ConferenceCall(Audio) Test</h3>
				</nav>
				<!-- End of Topbar -->

				<!-- Begin Page Content -->
				<div class="container-fluid">


					<br /> <br />

					<!-- Content Row -->
					<div class="row">

						<div class="col-sxl-4 col-lg-5">
							<div class="card shadow mb-4">
								<!-- Card Header - Dropdown -->
								<div
									class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
									<h6 class="m-0 font-weight-bold text-primary">Channels</h6>
								
								<!-- Card Body -->
								<div class="card-body">
									<div class="thumnail-wrapper">
										<div class="thumnail">
											<div class="centered">
												<img alt="프로필사진 없음" src="/resources/img/profile.jpg">
											</div>
										</div>
									</div>

									<main class="text-center"> <!-- 음성만 사용시 video가 아닌 audio로 설정. remote audio에 대하여 모두 개별적인 audio를 사용해야함.
                      localAudio는 muted로 처리해야함. 안그러면 하울링 남--> <audio
										id="myVideo" class="remote-video center w-300 h-300" autoplay
										muted controls playsinline
										style="z-index:1;background: rgba(0, 0, 0, 0.5); width: 100%;"></audio>

									<h6 id="waitingTv" class="m-0 font-weight-bold text-primary"
										style="z-index: 3; position: absolute; bottom: 55px; right: 45px; visibility: hidden;">
										waiting</h6>

									<div class="row">
										<div class="mt-12 text-center">
											<span class="mr-2"> <input id="channelNameInput"
												class="text-center" type="text" placeholder="channel name"
												autofocus value="" />
											</span> <span class="mr-2"> <a id="channelBtn" href="#"
												class="btn btn-primary btn-user text-center"> CREATE </a>
											</span>
										</div>
									</div>
									</main>
								</div>
							</div>
						</div>

						<div class="col-xl-4 col-lg-5">
							<div class="card shadow mb-4">
								<!-- Card Header - Dropdown -->
								<div
									class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
									<h6 class="m-0 font-weight-bold text-primary">Other Audios
									</h6>
								</div>
								<!-- Card Body -->
								<div class="card-body">

									<main class="text-center">
									<div class="row" id="otherAudios">
										<div class="thumnail-wrapper">
											<div class="thumnail">
												<div class="centered">
													<img alt="프로필사진 없음" src="/resources/img/profile.jpg">
												</div>
											</div>
										</div>
									</div>
									</main>
								</div>
							</div>
						</div>

					</div>
				</div>
				<!-- /.container-fluid -->

				<link rel="stylesheet"
					href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" />
				<!-- The webrtc adapter is required for browser compatibility -->
				<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
				<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk"></script>

				<script>
                const channelButton = document.querySelector("#channelBtn");
                const channelList = document.getElementById("lvChannel");
                const channelNameInput = document.getElementById("channelNameInput");
                const waitingTv = document.getElementById("waitingTv");
                const videoInputSelect = document.getElementById(
                    "videoInputSelect"
                );
                const audioInputSelect = document.getElementById(
                    "audioInputSelect"
                );
                const videoCodecSelect = document.getElementById(
                    "videoCodecSelect"
                );
                const resolutionSelect = document.getElementById(
                    "resolutionSelect"
                );
                const otherAudios = document.getElementById('otherAudios');
                const videoFPSInput = document.getElementById("videoFPSInput");
                let isConnected = false;
                let isCaster = false;
                let myChannelId;
                let remon;
                let viewerMap = {}
                let dummyRemon;
                const key = "1234567890";
                const serviceId = "SERVICEID1";
                var myRoomChId;
                var waitingLoop;
                let cameraList = [];
                let micList = [];
                createDummyRemonForSearchLoop();
                startSearchLoop();

                // please register your own service key from remotemonster site.
                let config = {
                    credential: {
                        key: 'ca804846fa2f723da39769e566573a7452b7dce2a769befcd933a5530934f9cf',
                        serviceId: '5122057a-631a-4ba4-8111-7b3457480d7d',
                        wsurl: "wss://signal.remotemonster.com/ws",
                        resturl: "https://signal.remotemonster.com/rest",

                    },
                    view: {
                        //remote: "#remoteVideo",
                        local: "#myVideo"
                    },
                    media: {
                        video: false,
                        audio: true
                    }
                    /* 주변소음을 제거하고 음성을 전달하는데 초점을 맞춤 */
                    rtc: {audioType: "voice"}
                };

                const listener = {
                    onConnect(chid) {
                        console.log('remon.listener.onConnect ${chid} at listener');
                    },
                    onComplete() {
                        myRoomChId = remon.getChannelId();
                        console.log("remon.listener.onComplete " + remon.getChannelId());
                        setViewsViaParameters(false, "hidden", "CLOSE", "hidden");
                    },
                    onDisconnectChannel() {
                        // is called when other peer hang up.
                        console.log('remon.listener.onDisconnectChannel ${remon.getChannelId()}')
                        remon.close();
                        isConnected = false;
                        setViewsViaParameters(false, "hidden", "CREATE", "visible");
                    },
                    onClose() {
                        // is called when remon.close() method is called.
                        console.log('remon.listener.onClose: ${remon.getChannelId()}');
                        remon.close();

                        isConnected = false;
                        setViewsViaParameters(false, "hidden", "CREATE", "visible");
                    },
                    onError(error) {
                        console.log('remon.listener.onError: ${remon.getChannelId()} ${error}');
                    },
                    onStat(result) {
                        // console.log(`EVENT FIRED: onStat: ${result}`);
                    }
                };
                let viewerConfig = {
                    credential: {
                        key: key,
                        serviceId: serviceId,
                        wsurl: "wss://signal.remotemonster.com/ws",
                        resturl: "https://signal.remotemonster.com/rest"

                    },
                    view: {
                        remote: null,
                        local: null
                    },
                   
                    media: {
                        video: {
                          width: "320",
                          height: "240",
                          codec: "h264",
                          maxBandwidth: 100,
                          frameRate: { max: 25, min: 25 }
                        },
                        audio: true
                      }
                };
                const getViewerlistener = (id) => {

                    return {
                        onConnect(chid) {
                            // console.log(`EVENT FIRED:VIEWER onConnect: ${chid}`);
                        },
                        onComplete() {
                            console.log('getViewerListener.onComplete: ${viewerMap[id].remon.getChannelId()}');
                            // console.log("EVENT FIRED:VIEWER onComplete");
                        },
                        onDisconnectChannel() {
                            // is called when other peer hang up.
                            console.log('getViewerListener.onDisconnectChannel: ${viewerMap[id].remon.getChannelId()}');
                            //viewerMap[id].remon.close()
                            isConnected = false;
                            // console.log("EVENT FIRED:VIEWER onDisconnectChannel");
                        },
                        onClose() {
                            console.log('getViewerListener.onClose: ${viewerMap[id].remon.getChannelId()}');
                            // is called when remon.close() method is called.
                            //viewerMap[id].remon.close()
                            // console.log("EVENT FIRED:VIEWER onClose");
                        },
                        onError(error) {
                            console.log('getViewerListener.onError: ${viewerMap[id].remon.getChannelId()}');
                        },
                        onStat(result) {
                            // console.log(`EVENT FIRED:VIEWER onStat: ${result}`);
                        }
                    }
                }

                function setViewsViaParameters(
                    runWaitLoop,
                    waitingTvVisibility,
                    btnText,
                    inputVisiblility
                ) {
                    if (runWaitLoop) {
                        waitingMsgLoop();
                    } else {
                        clearInterval(waitingLoop);
                    }
                    waitingTv.style.visibility = waitingTvVisibility;
                    channelButton.innerHTML = btnText;
                    channelNameInput.style.visibility = inputVisiblility;
                }

                function start() {
                    if (isConnected) {
                        isConnected = false;
                        isCaster = false;
                        Object.keys(viewerMap).forEach(videoId => {
                            viewerMap[videoId].remon && viewerMap[videoId].remon.close()
                        });
                        remon.close();
                        myChannelId = "";
                        setViewsViaParameters(false, "hidden", "CREATE", "visible");
                    } else {
                        isConnected = true;
                        isCaster = true;

                        remon = new Remon({config, listener});
                        myChannelId = channelNameInput.value
                            ? channelNameInput.value
                            : getRandomId();
                        remon.createRoom(myChannelId);
                        setViewsViaParameters(true, "visible", "CLOSE", "hidden");
                    }
                }

                function getRandomId() {
                    var text = "";
                    var possible =
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    for (var i = 0; i < 5; i++)
                        text += possible.charAt(
                            Math.floor(Math.random() * possible.length)
                        );
                    return text;
                }

                channelButton.addEventListener(
                    "click",
                    evt => {
                        start(false);
                        evt.preventDefault();
                    },
                    false
                );

                function createDummyRemonForSearchLoop() {
                    if (dummyRemon) dummyRemon.close();
                    let cfg = {
                        credential: {key: key, serviceId: serviceId},
                        view: {local: "#localVideo1", remote: "#remoteVideo1"},
                        media: {audio: true, video: true}
                    };
                    // cfg.credential.wsurl = "ws://localhost:8082/ws";
                    // cfg.credential.resturl = "http://localhost:8081/rest";
                    cfg.credential.wsurl = "wss://signal.remotemonster.com/ws";
                    cfg.credential.resturl = "https://signal.remotemonster.com/rest";

                    dummyRemon = new Remon({config: cfg});
                }

                function waitingMsgLoop() {
                    var numOfPoint = 0;
                    var pointStr = "";
                    waitingLoop = setInterval(async function () {
                        pointStr = "";
                        numOfPoint++;
                        if (numOfPoint === 4) numOfPoint = 0;
                        for (var i = 0; i < numOfPoint; i++) {
                            pointStr += ".";
                        }
                        waitingTv.innerText = "waiting" + pointStr;
                    }, 3000);
                }

                function startSearchLoop() {
                    setInterval(async function () {
                        dummyRemon.config.credential.serviceId = serviceId;
                        dummyRemon.config.credential.key = key;
                        var searchResult = await dummyRemon.fetchRooms(channelNameInput.value);
                        Object.keys(viewerMap).forEach(videoId => {
                            if (!searchResult.map(x => x.id).includes(videoId.replace("-", ":"))) {
                                viewerMap[videoId].remon && viewerMap[videoId].remon.close()
                                document.getElementById(videoId).parentNode.remove();
                                document.getElementById("btn-" + videoId).parentNode.remove()
                                delete viewerMap[videoId]
                            }
                        })
                        searchResult.forEach(({id}, i) => {
                            var chid = id;
                            id = id.replace(":", "-");
                            //console.log(id)
                            //TODO:
                            if (chid !== myRoomChId && document.getElementById(id) == null) {
                                const audioAttrs = {
                                    id: id,
                                    class: "remote-video center w-320 h-240",
                                    autoplay: true,
                                    controls: true,
                                    playsinline: true,
                                    style: "z-index:1;background: rgba(0, 0, 0, 0.5); width: 220px;"
                                }
                                let newAudio = document.createElement('audio')
                                Object.keys(audioAttrs).forEach(key => newAudio.setAttribute(key, audioAttrs[key]))
                                viewerMap[newAudio.id] = newAudio
                                otherAudios.appendChild(newAudio)

                                let btn = document.createElement("input");
                                btn.type = "button";
                                btn.id = "btn-" + id
                                btn.name = id;
                                btn.value = id;
                                btn.className = "btn btn-primary btn-user btn-block text-center";
                                btn.addEventListener(
                                    "click",
                                    evt => {
                                        console.log('new remote audio id = ${newAudio.id}');
                                        viewerConfig.view.remote = '#${newAudio.id}'
                                        newAudio.remon = new Remon({
                                            config: viewerConfig,
                                            listener: getViewerlistener(newAudio.id)
                                        })
                                        newAudio.remon.joinCast(newAudio.id.replace("-", ":"))

                                    },
                                    false
                                );
                         
                            }
                        });
                    }, 4000);
                }

               

            </script>
			</div>
			<!-- End of Main Content -->
		</div>
		<!-- End of Content Wrapper -->
	</div>
	<!-- End of Page Wrapper -->

	<!-- Bootstrap core JavaScript-->
	<script src="../resources/vendor/jquery/jquery.min.js"></script>
	<script src="../resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

	<!-- Core plugin JavaScript-->
	<script src="../resources/vendor/jquery-easing/jquery.easing.min.js"></script>

	<!-- Custom scripts for all pages-->
	<script src="../resources/js/sb-admin-2.min.js"></script>

</body>
</html>