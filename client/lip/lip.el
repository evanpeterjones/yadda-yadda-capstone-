;;; lip.el --- a social media client for Emacs.

;; Copyright (C) 2015-2018  Free Software Foundation, Inc.

;; Author: Evan Jones <evanpeterjones@gmail.com>
;; Maintainer: &
;; URL: https://github.com/evanpeterjones/yadda-yadda/client/lip
;; Version: 0.0.01
;; Keywords: lip yapp yadda-yadda
;; Package-Requires: ((emacs "24"))

(if (version< emacs-version "24")
    (message "this package requires Emacs 24.4 or later"))

(defvar user-session-id nil)

(defvar deps
  '(request)
  "a growing list of needed dependencies ~redundant? lol")

(defun all (test-list)
  "recursively verify no values in a list are nil"
  (if test-list
      (and (car test-list) (and-all (cdr test-list)))
    t))

(require 'package)
;; if any packages not installed, install

(defun make-request (endpoint)
  (with-current-buffer (url-retrieve-synchronously endpoint)
    (progn 
      (print (buffer-string))
      (kill-buffer))))

(request "localhost:5000/feed"
         :type "GET"
         :success (cl-function  (lambda (x) (print x)))
         :error (cl-function (lambda (x) (print x)))
         :status-code '((400 . (lambda (&rest _) (message "Got 400.")))
                        (404 . (lambda (&rest _) (message "Got 404.")))
                        (418 . (lambda (&rest _) (message "Got 418.")))))

