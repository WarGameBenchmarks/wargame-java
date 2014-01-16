<?php

function get_key($key = null) {
	if ($key == null) return null;
	if ( isset($_GET[$key]) && !empty($_GET[$key]) ) return $_GET[$key];
	return null;
}

$keys = array(
	'fsc',
	'fsp',
	'tds',
	'elt',
	'gcd'
);

$values = array();

foreach ($keys as $key) {
	$v = get_key($key);
	if ( $v == null || !is_numeric($v) ) {
		$values[$key] = '';
	}
	$values[$key] = $v;
}

header("Location: https://docs.google.com/forms/d/1MjnuGdyrx2rqsK8JatyDdR0Hg6Wuk9dX0f5Sh6ziBsU/viewform?entry.2078160005={$values['fsc']}&entry.992939736={$values['fsp']}&entry.1042136503={$values['tds']}&entry.337763981={$values['elt']}&entry.28122394={$values['gcd']}");
exit();

?>