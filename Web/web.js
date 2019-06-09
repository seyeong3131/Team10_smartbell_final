const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const session = require('express-session');

var FCM = require('fcm-node');
var fcm = new FCM("AAAAxMHSmYI:APA91bGCGU3KWor7047u7cUWOn8dcBo1FwTzmgRVCeW9dlG4HbM1biXmzD193gYxisPJBKSC9FmXXe7l2Pzy4_6LgQdyfcoZS-vKc5plaO5IEAGFnsYxz0NCVVYAdflRETYDPpJX-IX2");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(session({
	secret: '@#@$Secret#@$#$',
	resave: false,
	saveUninitialized: true
}));


app.use('/index', express.static(__dirname + '/index.html'));

app.listen(8001, () => { console.info('Express server has started on port 8001'); });


var orders = {}

app.get('/order', (req, res) => {
	orders[req.query.token] = req.query.menus;
	res.status(200).send(true);
});

app.get('/orders', (req, res) => {
	res.status(200).send(JSON.stringify(orders));
});

app.get('/completed', (req, res) => {	
	var data = {
		to: req.query.token,
		notification: {
			title: "SmartBell",
			body: "메뉴가 준비되었습니다.",
			sound: "default"
		},
		priority: "high",
		restricted_package_name: "team10.smartbell",
		data: {
		}
	};

	fcm.send(data, function(err, response) {
		if (orders[req.query.token] == null) return
		delete orders[req.query.token]

		if (err) {
			console.error('메시지 발송에 실패했습니다.');
			console.error(err);
			res.status(200).send(err);
			return;
		}
	 
		console.log('메시지가 발송되었습니다.');
		console.log(response);
		res.status(200).send(true);
	});
})